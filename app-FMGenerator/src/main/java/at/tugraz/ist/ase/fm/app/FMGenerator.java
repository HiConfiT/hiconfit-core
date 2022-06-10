/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.app;

import at.tugraz.ist.ase.fm.app.cli.FMGenerator_CmdLineOptions;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.fm.parser.SXFMParser;
import at.tugraz.ist.ase.kb.fm.FMKB;
import es.us.isa.FAMA.models.FAMAfeatureModel.FAMAFeatureModel;
import es.us.isa.FAMA.models.variabilityModel.VariabilityModel;
import es.us.isa.generator.FM.AbstractFMGenerator;
import es.us.isa.generator.FM.Evolutionay.EvolutionaryFMGenerator;
import es.us.isa.generator.FM.Evolutionay.FitnessFunction;
import es.us.isa.generator.FM.GeneratorCharacteristics;
import es.us.isa.utils.FMStatistics;
import es.us.isa.utils.FMWriter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates synthesized feature models using the Betty framework.
 * For further details on Betty framework, we refer to <a href="https://www.isa.us.es/betty/welcome">https://www.isa.us.es/betty/welcome</a>
 *
 * The number of features in each generated feature model is specified by the following formula:
 * numFeatures = numConstraints * 3 / 4;
 *
 * for numFeatures < 10, using Random generation
 * for numFeatures >= 10, using an evolutionary generator
 *
 * Generated feature models are saved using the SPLOT format (<a href="http://www.splot-research.org">http://www.splot-research.org</a>).
 */
public class FMGenerator {

    static String welcome = """
              █████▒███▄ ▄███▓     ▄████ ▓█████  ███▄    █\s
            ▓██   ▒▓██▒▀█▀ ██▒    ██▒ ▀█▒▓█   ▀  ██ ▀█   █\s
            ▒████ ░▓██    ▓██░   ▒██░▄▄▄░▒███   ▓██  ▀█ ██▒
            ░▓█▒  ░▒██    ▒██    ░▓█  ██▓▒▓█  ▄ ▓██▒  ▐▌██▒
            ░▒█░   ▒██▒   ░██▒   ░▒▓███▀▒░▒████▒▒██░   ▓██░
             ▒ ░   ░ ▒░   ░  ░    ░▒   ▒ ░░ ▒░ ░░ ▒░   ▒ ▒\s
             ░     ░  ░      ░     ░   ░  ░ ░  ░░ ░░   ░ ▒░
             ░ ░   ░      ░      ░ ░   ░    ░      ░   ░ ░\s
                          ░            ░    ░  ░         ░\s
                                                          \s""";
    static String programTitle = "Synthesized Feature Model Generator";
    static String usage = "Usage: java -jar fm_gen.jar [options]";

    public static void main(String[] args) {

        FMGenerator_CmdLineOptions cmdLineOptions = new FMGenerator_CmdLineOptions(welcome, programTitle, null, usage);
        cmdLineOptions.parseArgument(args);

        if (cmdLineOptions.isHelp()) {
            cmdLineOptions.printUsage();
            System.exit(0);
        }

        cmdLineOptions.printWelcome();

        FMGenerator fmGenerator = new FMGenerator(cmdLineOptions);
        try {
            fmGenerator.generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nDONE.");
    }

    FMGenerator_CmdLineOptions options;

    public FMGenerator(FMGenerator_CmdLineOptions options) {
        this.options = options;
    }

    public void generate() throws Exception {
        int numConstraints = options.getNumConstraints();
        int numGenFM = options.getNumGenFMs();
        double ctc = options.getCTC();
        int maxGenerations = options.getMaxGenerations();
        String outDir = options.getOutFolder();

        // TODO: multi-threading
        for (int i = 0; i < numGenFM; i++) {
            String modelName = "FM_" + numConstraints + "_" + i;
            String filename = modelName + ".splx";
            String outFile = outDir + "/" + filename;

            System.out.println("\nGenerating feature model " + (i + 1) + " with name " + filename.toUpperCase());

            int numFeatures = numConstraints * 3 / 4;
            System.out.println("\tNumber of features: " + numFeatures);

            int count = 0;
            if (numFeatures < 10) { // for numFeatures < 10, using Random generation

                do {
                    count++;
                    System.out.println("\tTry " + count);

                    // STEP 1: Specify the user's preferences for the generation (characteristics)
                    GeneratorCharacteristics characteristics = getGeneratorCharacteristics(numFeatures, ctc, modelName);

                    // STEP 2: Generate the model with the specific characteristics (FaMa FM metamodel is used)
                    AbstractFMGenerator generator = new es.us.isa.generator.FM.FMGenerator();
                    FAMAFeatureModel fm = (FAMAFeatureModel) generator.generateFM(characteristics);

                    // STEP 3: Save the model and the products
                    saveFM(modelName, outFile, fm);

                    // check the number of constraints
                } while (isNotEnoughConstraints(outFile, numConstraints));

            } else { // for numFeatures >= 10, using an evolutionary generator

                do {
                    count++;
                    System.out.println("\tTry " + count);

                    // STEP 1: Specify the user's preferences for the generation (characteristics)
                    GeneratorCharacteristics characteristics = getGeneratorCharacteristics(numFeatures, ctc, modelName);

                    // STEP 2: Generate the model with the specific characteristics (FaMa FM metamodel is used)
                    EvolutionaryFMGenerator generator = new EvolutionaryFMGenerator();

                    //STEP 2.3: Set the fitness function for the genetic algorithm and the max number of generations allowed
                    NumberConstraintsFitness fitness = new NumberConstraintsFitness();
                    fitness.numConstraints = numConstraints;
                    generator.setFitnessFunction(fitness);
                    generator.setMaximize(true);
                    generator.setMaxGenerations(maxGenerations);

                    VariabilityModel geneticg = generator.generateFM(characteristics);

                    // STEP 3: Save the model and the products
                    saveFM(modelName, outFile, geneticg);

                    // check the number of constraints
                } while (isNotEnoughConstraints(outFile, numConstraints));
            }
            System.out.println("DONE - " + modelName);
        }
    }

    /**
     * Specify the user's preferences for the generation (characteristics).
     * @param numFeatures Number of features
     * @param ctc Cross-Tree Constraints ratio
     * @param modelName Name of the model
     * @return return a GeneratorCharacteristics object
     */
    private GeneratorCharacteristics getGeneratorCharacteristics(int numFeatures, double ctc, String modelName) {
        GeneratorCharacteristics characteristics = new GeneratorCharacteristics();
        characteristics.setNumberOfFeatures(numFeatures); // Number of features.
        characteristics.setPercentageCTC((int) (100 * ctc)); // Number of constraints.
        // Max number of products of the feature model to be generated. Too large values could cause memory overflows or the program getting stuck.
        characteristics.setMaxProducts(10000);

        characteristics.setModelName(modelName);
        return characteristics;
    }

    /**
     * Check if the number of constraints in the generated feature model is enough.
     * Delete the generated file if it is not enough constraints.
     *
     * @param filename the file name of the generated feature model
     * @param numExpCstrs the expected number of constraints
     * @return true if the number of constraints is enough, false otherwise
     */
    private boolean isNotEnoughConstraints(String filename, int numExpCstrs) throws FeatureModelParserException {
        SXFMParser parser = new SXFMParser();

        File file = new File(filename);
        FeatureModel featureModel = parser.parse(file);
        FMKB model = new FMKB(featureModel, false);

        if (model.getModelKB().getSolver().solve()) { // if the model is consistent
            int numGenCstrs = featureModel.getNumOfRelationships() + featureModel.getNumOfConstraints();
            System.out.println("\t\tNumber of features: " + featureModel.getNumOfFeatures());
            System.out.println("\t\tNumber of constraints: " + numGenCstrs);

            if (numExpCstrs == numGenCstrs) { // check the number of constraints
                return false;
            }
        }
        // delete file if still not enough constraints
        file.delete();
        return true;
    }

    private void saveFM(String modelName, String filename, VariabilityModel fm) throws Exception {
        FMWriter writer = new FMWriter();
        writer.saveFM(fm, filename);

        addFMName(filename, modelName); // add header
    }

    private void addFMName(String filename, String fmName) throws IOException {
        Path path = Paths.get(filename);
        Charset charset = StandardCharsets.UTF_8;
        List<String> lines = new ArrayList<>();
        if (Files.exists(path)) {
            lines = Files.readAllLines(path, charset);

            lines.remove(0);
            lines.add(0, "<feature_model name=\"" + fmName + "\">");
        }
        Files.write(path, lines, charset);
    }

    private static class NumberConstraintsFitness implements FitnessFunction {
        public int numConstraints;

        @Override
        public double fitness(FAMAFeatureModel fm) {
            FMStatistics statistics = new FMStatistics(fm);
            int total = statistics.getNoAlternative() + statistics.getNoMandatory() + statistics.getNoOptional() + statistics.getNoOr() + statistics.getNoRequires() + statistics.getNoExcludes();
//            System.out.println("total: " + total);
//            System.out.println("numConstraints: " + numConstraints);
            return (total > numConstraints) ? ((double)numConstraints / total) : ((double)total / numConstraints);
        }
    }
}
