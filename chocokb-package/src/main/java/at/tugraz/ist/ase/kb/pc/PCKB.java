/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.pc;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.*;
import at.tugraz.ist.ase.kb.core.builder.IntVarConstraintBuilder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class PCKB extends KB implements IIntVarKB {

    public PCKB(boolean hasNegativeConstraints) {
        super("PCConfigurationProblem", "https://www.itu.dk/research/cla/externals/clib/", hasNegativeConstraints);

        reset(hasNegativeConstraints);
    }

    @Override
    public void reset(boolean hasNegativeConstraints) {
        log.trace("{}Creating PCKB >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        modelKB = new Model(name);
        variableList = new LinkedList<>();
        domainList = new LinkedList<>();
        constraintList = new LinkedList<>();
        defineDomains();
        defineVariables();
        defineConstraints(hasNegativeConstraints);

        LoggerUtils.outdent();
        log.debug("{}<<< Created PCKB", LoggerUtils.tab());
    }

    private void defineDomains() {
        log.trace("{}Defining domains >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        domainList.add(Domain.builder()
                .name("mb_idD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_cpuslotD")
                .values(List.of("0", "1", "2", "3"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_controllertypeD")
                .values(List.of("0", "1", "2"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_SOME_slotD0")
                .values(List.of("0", "1", "2", "3"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_SOME_slotD1")
                .values(List.of("0", "1", "2", "3"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_SOME_slotD2")
                .values(List.of("0", "1", "2", "3"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_SOME_slotD3")
                .values(List.of("0", "1", "2", "3"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_SOME_slotD4")
                .values(List.of("0", "1", "2", "3"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_SOME_slotD5")
                .values(List.of("0", "1", "2", "3"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_SOME_slotD6")
                .values(List.of("0", "1", "2", "3"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_SOME_slotD7")
                .values(List.of("0", "1", "2", "3"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_controllercountD")
                .values(List.of("0", "1", "2", "3", "4"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_mincpufreqD")
                .values(List.of("200", "400", "600", "800", "1000", "1200", "1400", "1600", "1800", "2000", "2200", "2400"))
                .chocoValues(List.of(200, 400, 600, 800, 1000, 1200, 1400, 1600, 1800, 2000, 2200, 2400))
                .build());
        domainList.add(Domain.builder()
                .name("mb_maxcpufreqD")
                .values(List.of("200", "400", "600", "800", "1000", "1200", "1400", "1600", "1800", "2000", "2200", "2400"))
                .chocoValues(List.of(200, 400, 600, 800, 1000, 1200, 1400, 1600, 1800, 2000, 2200, 2400))
                .build());
        domainList.add(Domain.builder()
                .name("mb_ramslotD")
                .values(List.of("0", "1", "2"))
                .build());
        domainList.add(Domain.builder()
                .name("mb_ramcapacityD")
                .values(List.of("32", "64", "128", "256", "512", "1024", "2048", "4096"))
                .chocoValues(List.of(32, 64, 128, 256, 512, 1024, 2048, 4096))
                .build());

        domainList.add(Domain.builder()
                .name("pro_idD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6", "7"))
                .build());
        domainList.add(Domain.builder()
                .name("pro_cpuslotD")
                .values(List.of("0", "1", "2", "3"))
                .build());
        domainList.add(Domain.builder()
                .name("pro_freqD")
                .values(List.of("200", "400", "600", "800", "1000", "1200", "1400", "1600", "1800", "2000", "2200", "2400"))
                .chocoValues(List.of(200, 400, 600, 800, 1000, 1200, 1400, 1600, 1800, 2000, 2200, 2400))
                .build());

        domainList.add(Domain.builder()
                .name("hd1_idD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6"))
                .build());
        domainList.add(Domain.builder()
                .name("hd1_busD")
                .values(List.of("0", "1", "2"))
                .build());
        domainList.add(Domain.builder()
                .name("hd1_capacityD")
                .values(List.of("512", "1024", "2048", "4096", "8192", "16384", "32768"))
                .chocoValues(List.of(512, 1024, 2048, 4096, 8192, 16384, 32768))
                .build());

        domainList.add(Domain.builder()
                .name("hd2_idD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6"))
                .build());
        domainList.add(Domain.builder()
                .name("hd2_busD")
                .values(List.of("0", "1", "2"))
                .build());
        domainList.add(Domain.builder()
                .name("hd2_capacityD")
                .values(List.of("512", "1024", "2048", "4096", "8192", "16384", "32768"))
                .chocoValues(List.of(512, 1024, 2048, 4096, 8192, 16384, 32768))
                .build());

        domainList.add(Domain.builder()
                .name("ram1_idD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6"))
                .build());
        domainList.add(Domain.builder()
                .name("ram1_slotD")
                .values(List.of("0", "1", "2"))
                .build());
        domainList.add(Domain.builder()
                .name("ram1_capacityD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "16", "32", "64", "65", "67", "68", "100", "128",
                        "256", "512", "1024", "2000", "2048", "3000", "4096", "5000", "6000", "7000", "8192", "9000", "10000", "16384"))
                .chocoValues(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 32, 64, 65, 67, 68, 100, 128, 256, 512, 1024, 2000, 2048, 3000,
                        4096, 5000, 6000, 7000, 8192, 9000, 10000, 16384))
                .build());

        domainList.add(Domain.builder()
                .name("ram2_idD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6"))
                .build());
        domainList.add(Domain.builder()
                .name("ram2_slotD")
                .values(List.of("0", "1", "2"))
                .build());
        domainList.add(Domain.builder()
                .name("ram2_capacityD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "16", "32", "64", "65", "67", "68", "100", "128",
                        "256", "512", "1024", "2000", "2048", "3000", "4096", "5000", "6000", "7000", "8192", "9000", "10000", "16384"))
                .chocoValues(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 32, 64, 65, 67, 68, 100, 128, 256, 512, 1024, 2000, 2048, 3000,
                        4096, 5000, 6000, 7000, 8192, 9000, 10000, 16384))
                .build());

        domainList.add(Domain.builder()
                .name("ram3_idD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6"))
                .build());
        domainList.add(Domain.builder()
                .name("ram3_slotD")
                .values(List.of("0", "1", "2"))
                .build());
        domainList.add(Domain.builder()
                .name("ram3_capacityD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "16", "32", "64", "65", "67", "68", "100", "128",
                        "256", "512", "1024", "2000", "2048", "3000", "4096", "5000", "6000", "7000", "8192", "9000", "10000", "16384"))
                .chocoValues(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 32, 64, 65, 67, 68, 100, 128, 256, 512, 1024, 2000, 2048, 3000,
                        4096, 5000, 6000, 7000, 8192, 9000, 10000, 16384))
                .build());

        domainList.add(Domain.builder()
                .name("ram4_idD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6"))
                .build());
        domainList.add(Domain.builder()
                .name("ram4_slotD")
                .values(List.of("0", "1", "2"))
                .build());
        domainList.add(Domain.builder()
                .name("ram4_capacityD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "16", "32", "64", "65", "67", "68", "100", "128",
                        "256", "512", "1024", "2000", "2048", "3000", "4096", "5000", "6000", "7000", "8192", "9000", "10000", "16384"))
                .chocoValues(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 32, 64, 65, 67, 68, 100, 128, 256, 512, 1024, 2000, 2048, 3000,
                        4096, 5000, 6000, 7000, 8192, 9000, 10000, 16384))
                .build());

        domainList.add(Domain.builder()
                .name("graphics_idD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6"))
                .build());
        domainList.add(Domain.builder()
                .name("graphics_slotD")
                .values(List.of("0", "1", "2", "3"))
                .build());

        domainList.add(Domain.builder()
                .name("pc_clockD")
                .values(List.of("200", "400", "600", "800", "1000", "1200", "1400", "1600", "1800", "2000", "2200", "2400"))
                .chocoValues(List.of(200, 400, 600, 800, 1000, 1200, 1400, 1600, 1800, 2000, 2200, 2400))
                .build());
        domainList.add(Domain.builder()
                .name("pc_memoryD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "16", "32", "64", "65", "67", "68", "100", "128",
                        "256", "512", "1024", "2000", "2048", "3000", "4096", "5000", "6000", "7000", "8192", "9000", "10000", "16384"))
                .chocoValues(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 32, 64, 65, 67, 68, 100, 128, 256, 512, 1024, 2000, 2048, 3000,
                        4096, 5000, 6000, 7000, 8192, 9000, 10000, 16384))
                .build());

        domainList.add(Domain.builder()
                .name("ram1_presentD")
                .values(List.of("512", "1024"))
                .chocoValues(List.of(512, 1024))
                .build());
        domainList.add(Domain.builder()
                .name("ram2_presentD")
                .values(List.of("512", "1024"))
                .chocoValues(List.of(512, 1024))
                .build());
        domainList.add(Domain.builder()
                .name("ram3_presentD")
                .values(List.of("512", "1024"))
                .chocoValues(List.of(512, 1024))
                .build());
        domainList.add(Domain.builder()
                .name("ram4_presentD")
                .values(List.of("512", "1024"))
                .chocoValues(List.of(512, 1024))
                .build());

        LoggerUtils.outdent();
        log.trace("{}<<< Created domains", LoggerUtils.tab());
    }

    public void defineVariables (){
        log.trace("{}Defining variables >>", LoggerUtils.tab());
        LoggerUtils.indent();

        List<String> varNames = List.of("mb_idD", "mb_cpuslotD", "mb_controllertypeD", "mb_SOME_slotD0", "mb_SOME_slotD1",
                "mb_SOME_slotD2", "mb_SOME_slotD3", "mb_SOME_slotD4", "mb_SOME_slotD5", "mb_SOME_slotD6", "mb_SOME_slotD7", "mb_controllercountD",
                "mb_mincpufreqD", "mb_maxcpufreqD", "mb_ramslotD", "mb_ramcapacityD", "pro_idD", "pro_cpuslotD", "pro_freqD", "hd1_idD",
                "hd1_busD", "hd1_capacityD", "hd2_idD", "hd2_busD", "hd2_capacityD", "ram1_idD", "ram1_slotD", "ram1_capacityD", "ram2_idD",
                "ram2_slotD", "ram2_capacityD", "ram3_idD", "ram3_slotD", "ram3_capacityD", "ram4_idD", "ram4_slotD", "ram4_capacityD",
                "graphics_idD", "graphics_slotD", "pc_clockD", "pc_memoryD", "ram1_presentD", "ram2_presentD", "ram3_presentD", "ram4_presentD");

        IntStream.range(0, varNames.size()).forEachOrdered(i -> {
            String varName = varNames.get(i);
            IntVar intVar = this.modelKB.intVar(varName, domainList.get(i).getIntValues());
            Variable var = IntVariable.builder()
                    .name(varName)
                    .domain(domainList.get(i))
                    .chocoVar(intVar).build();
            variableList.add(var);
        });

        LoggerUtils.outdent();
        log.trace("{}<<< Created variables", LoggerUtils.tab());
    }

    public void defineConstraints(boolean hasNegativeConstraints) {
        log.trace("{}Defining constraints >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        int startIdx = 0;

        // Constraint 1
        List<org.chocosolver.solver.constraints.Constraint> kbReqList = new ArrayList<>();
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 6));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 400));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 1600));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 4096));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 3));

        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 5));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 400));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 1600));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 4096));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 3));

        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 8));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 200));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 1000));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 2048));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 0));

        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 8));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 200));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 1000));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 2048));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 0));

        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 10));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 200));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 800));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 2048));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 0));

        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 10));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 200));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 800));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 2048));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 0));

        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 400));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 2000));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 2048));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 3));

        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 9));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 400));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 1000));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 4096));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 0));

        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 400));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 1600));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 2048));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 3));

        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 7));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 400));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 1400));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 2048));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 0));

        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 1800));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 2200));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 2048));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 0));

        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 1800));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 2200));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 2048));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 0));

        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(0)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(11)).getChocoVar(),"=", 4));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(12)).getChocoVar(),"=", 1800));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(13)).getChocoVar(),"=", 2400));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(),"=", 2048));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", 1));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", 2));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", 3));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", 0));
        kbReqList.add(this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", 0));

        List<org.chocosolver.solver.constraints.Constraint> configurationList = IntStream.iterate(0, counter -> counter <= kbReqList.size() - 16, counter -> counter + 16)
                .mapToObj(counter -> this.modelKB.and(kbReqList.get(counter), kbReqList.get(1 + counter), kbReqList.get(2 + counter),
                        kbReqList.get(3 + counter), kbReqList.get(4 + counter), kbReqList.get(5 + counter), kbReqList.get(6 + counter),
                        kbReqList.get(7 + counter), kbReqList.get(8 + counter), kbReqList.get(9 + counter), kbReqList.get(10 + counter),
                        kbReqList.get(11 + counter), kbReqList.get(12 + counter), kbReqList.get(13 + counter), kbReqList.get(14 + counter),
                        kbReqList.get(15 + counter))).toList();
        /*for (int counter = 0; counter <= kbReqList.size() - 16; counter = counter + 16) {
            configurationList.add(this.modelKB.and(kbReqList.get(counter), kbReqList.get(1 + counter), kbReqList.get(2 + counter),
                    kbReqList.get(3 + counter), kbReqList.get(4 + counter), kbReqList.get(5 + counter), kbReqList.get(6 + counter), kbReqList.get(7 + counter),
                    kbReqList.get(8 + counter), kbReqList.get(9 + counter), kbReqList.get(10 + counter), kbReqList.get(11 + counter), kbReqList.get(12 + counter),
                    kbReqList.get(13 + counter), kbReqList.get(14 + counter), kbReqList.get(15 + counter)));
        }*/

        org.chocosolver.solver.constraints.Constraint chocoConstraint = this.modelKB.or(configurationList.get(0), configurationList.get(1), configurationList.get(2), configurationList.get(3), configurationList.get(4),
                configurationList.get(5), configurationList.get(6), configurationList.get(7), configurationList.get(8), configurationList.get(9), configurationList.get(10),
                configurationList.get(11), configurationList.get(12));

//        addConstraint("constraint 1", chocoConstraint, startIdx, hasNegativeConstraints);
        Constraint constraint = IntVarConstraintBuilder.build("constraint 1", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 2
        startIdx = modelKB.getNbCstrs();
        chocoConstraint =  this.modelKB.or(
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(16)).getChocoVar(),"=", 6), this.modelKB.arithm(((IntVariable)variableList.get(17)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(18)).getChocoVar(),"=", 600)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(16)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(17)).getChocoVar(),"=", 3), this.modelKB.arithm(((IntVariable)variableList.get(18)).getChocoVar(),"=", 2400)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(16)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(17)).getChocoVar(),"=", 3), this.modelKB.arithm(((IntVariable)variableList.get(18)).getChocoVar(),"=", 1800)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(16)).getChocoVar(),"=", 7), this.modelKB.arithm(((IntVariable)variableList.get(17)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(18)).getChocoVar(),"=", 200)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(16)).getChocoVar(),"=", 3), this.modelKB.arithm(((IntVariable)variableList.get(17)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(18)).getChocoVar(),"=", 1200)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(16)).getChocoVar(),"=", 4), this.modelKB.arithm(((IntVariable)variableList.get(17)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(18)).getChocoVar(),"=", 1000)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(16)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(17)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(18)).getChocoVar(),"=", 2000)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(16)).getChocoVar(),"=", 5), this.modelKB.arithm(((IntVariable)variableList.get(17)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(18)).getChocoVar(),"=", 800)));

//        addConstraint("constraint 2", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 2", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 3
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(19)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(20)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(21)).getChocoVar(),"=", 512)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(19)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(20)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(21)).getChocoVar(),"=", 512)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(19)).getChocoVar(),"=", 5), this.modelKB.arithm(((IntVariable)variableList.get(20)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(21)).getChocoVar(),"=", 2048)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(19)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(20)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(21)).getChocoVar(),"=", 16384)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(19)).getChocoVar(),"=", 3), this.modelKB.arithm(((IntVariable)variableList.get(20)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(21)).getChocoVar(),"=", 8192)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(19)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(20)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(21)).getChocoVar(),"=", 32768)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(19)).getChocoVar(),"=", 6), this.modelKB.arithm(((IntVariable)variableList.get(20)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(21)).getChocoVar(),"=", 1024)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(19)).getChocoVar(),"=", 4), this.modelKB.arithm(((IntVariable)variableList.get(20)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(21)).getChocoVar(),"=", 4096)));

//        addConstraint("constraint 3", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 3", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 4
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(22)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(23)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(24)).getChocoVar(),"=", 512)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(22)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(23)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(24)).getChocoVar(),"=", 512)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(22)).getChocoVar(),"=", 5), this.modelKB.arithm(((IntVariable)variableList.get(23)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(24)).getChocoVar(),"=", 2048)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(22)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(23)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(24)).getChocoVar(),"=", 16384)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(22)).getChocoVar(),"=", 3), this.modelKB.arithm(((IntVariable)variableList.get(23)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(24)).getChocoVar(),"=", 8192)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(22)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(23)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(24)).getChocoVar(),"=", 32768)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(22)).getChocoVar(),"=", 6), this.modelKB.arithm(((IntVariable)variableList.get(23)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(24)).getChocoVar(),"=", 1024)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(22)).getChocoVar(),"=", 4), this.modelKB.arithm(((IntVariable)variableList.get(23)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(24)).getChocoVar(),"=", 4096)));

//        addConstraint("constraint 4", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 4", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 5
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(26)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(27)).getChocoVar(),"=", 0)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(26)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(27)).getChocoVar(),"=", 0)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),"=", 6), this.modelKB.arithm(((IntVariable)variableList.get(26)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(27)).getChocoVar(),"=", 1)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(26)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(27)).getChocoVar(),"=", 4)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),"=", 5), this.modelKB.arithm(((IntVariable)variableList.get(26)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(27)).getChocoVar(),"=", 1)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),"=", 4), this.modelKB.arithm(((IntVariable)variableList.get(26)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(27)).getChocoVar(),"=", 2)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),"=", 3), this.modelKB.arithm(((IntVariable)variableList.get(26)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(27)).getChocoVar(),"=", 2)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(26)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(27)).getChocoVar(),"=", 8)));

//        addConstraint("constraint 5", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 5", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 6
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(29)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(30)).getChocoVar(),"=", 0)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(29)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(30)).getChocoVar(),"=", 0)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),"=", 6), this.modelKB.arithm(((IntVariable)variableList.get(29)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(30)).getChocoVar(),"=", 1)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(29)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(30)).getChocoVar(),"=", 4)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),"=", 5), this.modelKB.arithm(((IntVariable)variableList.get(29)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(30)).getChocoVar(),"=", 1)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),"=", 4), this.modelKB.arithm(((IntVariable)variableList.get(29)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(30)).getChocoVar(),"=", 2)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),"=", 3), this.modelKB.arithm(((IntVariable)variableList.get(29)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(30)).getChocoVar(),"=", 2)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(29)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(30)).getChocoVar(),"=", 8)));

//        addConstraint("constraint 6", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 6", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 7
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(32)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(33)).getChocoVar(),"=", 0)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(32)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(33)).getChocoVar(),"=", 0)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),"=", 6), this.modelKB.arithm(((IntVariable)variableList.get(32)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(33)).getChocoVar(),"=", 1)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(32)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(33)).getChocoVar(),"=", 4)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),"=", 5), this.modelKB.arithm(((IntVariable)variableList.get(32)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(33)).getChocoVar(),"=", 1)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),"=", 4), this.modelKB.arithm(((IntVariable)variableList.get(32)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(33)).getChocoVar(),"=", 2)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),"=", 3), this.modelKB.arithm(((IntVariable)variableList.get(32)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(33)).getChocoVar(),"=", 2)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(32)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(33)).getChocoVar(),"=", 8)));

//        addConstraint("constraint 7", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 7", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 8
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(35)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(36)).getChocoVar(),"=", 0)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(35)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(36)).getChocoVar(),"=", 0)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),"=", 6), this.modelKB.arithm(((IntVariable)variableList.get(35)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(36)).getChocoVar(),"=", 1)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(35)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(36)).getChocoVar(),"=", 4)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),"=", 5), this.modelKB.arithm(((IntVariable)variableList.get(35)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(36)).getChocoVar(),"=", 1)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),"=", 4), this.modelKB.arithm(((IntVariable)variableList.get(35)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(36)).getChocoVar(),"=", 2)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),"=", 3), this.modelKB.arithm(((IntVariable)variableList.get(35)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(36)).getChocoVar(),"=", 2)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(35)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(36)).getChocoVar(),"=", 8)));

//        addConstraint("constraint 8", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 8", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 9
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(37)).getChocoVar(),"=", 0), this.modelKB.arithm(((IntVariable)variableList.get(38)).getChocoVar(),"=", 3)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(37)).getChocoVar(),"=", 3), this.modelKB.arithm(((IntVariable)variableList.get(38)).getChocoVar(),"=", 3)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(37)).getChocoVar(),"=", 6), this.modelKB.arithm(((IntVariable)variableList.get(38)).getChocoVar(),"=", 1)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(37)).getChocoVar(),"=", 2), this.modelKB.arithm(((IntVariable)variableList.get(38)).getChocoVar(),"=", 3)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(37)).getChocoVar(),"=", 5), this.modelKB.arithm(((IntVariable)variableList.get(38)).getChocoVar(),"=", 1)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(37)).getChocoVar(),"=", 4), this.modelKB.arithm(((IntVariable)variableList.get(38)).getChocoVar(),"=", 3)),
                this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(37)).getChocoVar(),"=", 1), this.modelKB.arithm(((IntVariable)variableList.get(38)).getChocoVar(),"=", 3)));

//        addConstraint("constraint 9", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 9", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 10
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.and(this.modelKB.arithm(((IntVariable)variableList.get(1)).getChocoVar(),"=", ((IntVariable)variableList.get(17)).getChocoVar()), this.modelKB.arithm(((IntVariable)variableList.get(18)).getChocoVar(),">=", ((IntVariable)variableList.get(12)).getChocoVar()), this.modelKB.arithm(((IntVariable)variableList.get(18)).getChocoVar(),"<=", ((IntVariable)variableList.get(13)).getChocoVar()));

//        addConstraint("constraint 10", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 10", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 11
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", ((IntVariable)variableList.get(20)).getChocoVar());

//        addConstraint("constraint 11", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 11", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 12
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.arithm(((IntVariable)variableList.get(2)).getChocoVar(),"=", ((IntVariable)variableList.get(23)).getChocoVar());

//        addConstraint("constraint 12", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 12", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 13
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", ((IntVariable)variableList.get(26)).getChocoVar());

//        addConstraint("constraint 13", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 13", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 14
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", ((IntVariable)variableList.get(29)).getChocoVar());

//        addConstraint("constraint 14", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 14", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 15
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", ((IntVariable)variableList.get(32)).getChocoVar());

//        addConstraint("constraint 15", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 15", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 16
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.arithm(((IntVariable)variableList.get(14)).getChocoVar(),"=", ((IntVariable)variableList.get(35)).getChocoVar());

//        addConstraint("constraint 16", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 16", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 17
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.arithm(((IntVariable)variableList.get(41)).getChocoVar(),"=", 512), this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),">", 0));

//        addConstraint("constraint 17", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 17", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 18
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.arithm(((IntVariable)variableList.get(41)).getChocoVar(),"=", 1024), this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),"<", 1));

//        addConstraint("constraint 18", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 18", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 19
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.arithm(((IntVariable)variableList.get(42)).getChocoVar(),"=", 512), this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),">", 0));

//        addConstraint("constraint 19", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 19", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 20
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.arithm(((IntVariable)variableList.get(42)).getChocoVar(),"=", 1024), this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),"<", 1));

//        addConstraint("constraint 20", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 20", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 21
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.arithm(((IntVariable)variableList.get(43)).getChocoVar(),"=", 512), this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),">", 0));

//        addConstraint("constraint 21", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 21", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 22
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.arithm(((IntVariable)variableList.get(43)).getChocoVar(),"=", 1024), this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),"<", 1));

//        addConstraint("constraint 22", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 22", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 23
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.arithm(((IntVariable)variableList.get(44)).getChocoVar(),"=", 512), this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),">", 0));

//        addConstraint("constraint 23", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 23", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 24
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.arithm(((IntVariable)variableList.get(44)).getChocoVar(),"=", 1024), this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),"<", 1));

//        addConstraint("constraint 24", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 24", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 25
        startIdx = modelKB.getNbCstrs();
        IntVar [] varsToBeAdded = new IntVar[4];
        IntVar sum = this.modelKB.intVar("sum", 0, 4097);
        varsToBeAdded[0] = ((IntVariable)variableList.get(41)).getChocoVar();
        varsToBeAdded[1] = ((IntVariable)variableList.get(42)).getChocoVar();
        varsToBeAdded[2] = ((IntVariable)variableList.get(43)).getChocoVar();
        varsToBeAdded[3] = ((IntVariable)variableList.get(44)).getChocoVar();
        this.modelKB.sum(varsToBeAdded ,"=", sum).post();
        chocoConstraint = this.modelKB.arithm(((IntVariable)variableList.get(15)).getChocoVar(), ">=", sum);

//        addConstraint("constraint 25", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 25", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 26
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.not(this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),">", 0)), this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),">", 0));

//        addConstraint("constraint 26", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 26", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 27
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.not(this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),">", 0)), this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),">", 0));

//        addConstraint("constraint 27", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 27", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 28
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.not(this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),">", 0)), this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),">", 0));

//        addConstraint("constraint 28", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 28", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 29
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.not(this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),">", 0)), this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(),">", 0));

//        addConstraint("constraint 29", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 29", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 30
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.not(this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),">", 0)), this.modelKB.arithm(((IntVariable)variableList.get(28)).getChocoVar(),">", 0));

//        addConstraint("constraint 30", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 30", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 31
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.not(this.modelKB.arithm(((IntVariable)variableList.get(34)).getChocoVar(),">", 0)), this.modelKB.arithm(((IntVariable)variableList.get(31)).getChocoVar(),">", 0));

//        addConstraint("constraint 31", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 31", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 32
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.or(this.modelKB.arithm(((IntVariable)variableList.get(3)).getChocoVar(),"=", ((IntVariable)variableList.get(38)).getChocoVar()), this.modelKB.arithm(((IntVariable)variableList.get(4)).getChocoVar(),"=", ((IntVariable)variableList.get(38)).getChocoVar()),
                this.modelKB.arithm(((IntVariable)variableList.get(5)).getChocoVar(),"=", ((IntVariable)variableList.get(38)).getChocoVar()), this.modelKB.arithm(((IntVariable)variableList.get(6)).getChocoVar(),"=", ((IntVariable)variableList.get(38)).getChocoVar()),
                this.modelKB.arithm(((IntVariable)variableList.get(7)).getChocoVar(),"=", ((IntVariable)variableList.get(38)).getChocoVar()), this.modelKB.arithm(((IntVariable)variableList.get(8)).getChocoVar(),"=", ((IntVariable)variableList.get(38)).getChocoVar()),
                this.modelKB.arithm(((IntVariable)variableList.get(9)).getChocoVar(),"=", ((IntVariable)variableList.get(38)).getChocoVar()), this.modelKB.arithm(((IntVariable)variableList.get(10)).getChocoVar(),"=", ((IntVariable)variableList.get(38)).getChocoVar()));

//        addConstraint("constraint 32", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 32", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 33
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.arithm(((IntVariable)variableList.get(19)).getChocoVar(), ">", 0);

//        addConstraint("constraint 33", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 33", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 34
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.arithm(((IntVariable)variableList.get(25)).getChocoVar(), ">", 0);

//        addConstraint("constraint 34", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 34", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 35
        startIdx = modelKB.getNbCstrs();
        chocoConstraint = this.modelKB.arithm(((IntVariable)variableList.get(39)).getChocoVar(), ">", ((IntVariable)variableList.get(18)).getChocoVar());

//        addConstraint("constraint 35", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 35", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        // Constraint 36
        startIdx = modelKB.getNbCstrs();
        IntVar [] varsToBeAdded2 = new IntVar[4];
        IntVar sum2 = this.modelKB.intVar("sum2", 0, 65536);
        varsToBeAdded2[0] = ((IntVariable)variableList.get(27)).getChocoVar();
        varsToBeAdded2[1] = ((IntVariable)variableList.get(30)).getChocoVar();
        varsToBeAdded2[2] = ((IntVariable)variableList.get(33)).getChocoVar();
        varsToBeAdded2[3] = ((IntVariable)variableList.get(36)).getChocoVar();
        this.modelKB.sum(varsToBeAdded2 ,"=",sum2).post();
        chocoConstraint = this.modelKB.arithm(((IntVariable)variableList.get(40)).getChocoVar(), "=", sum2);

//        addConstraint("constraint 36", chocoConstraint, startIdx, hasNegativeConstraints);
        constraint = IntVarConstraintBuilder.build("constraint 36", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        LoggerUtils.outdent();
        log.trace("{}<<< Created constraints", LoggerUtils.tab());
    }

//    private void addConstraint(String constraintName, org.chocosolver.solver.constraints.Constraint chocoConstraint, int startIdx, boolean hasNegativeConstraints) {
//        modelKB.post(chocoConstraint);
//
//        org.chocosolver.solver.constraints.Constraint negChocoConstraint = null;
//        if (hasNegativeConstraints) {
//            negChocoConstraint = chocoConstraint.getOpposite();
//            modelKB.post(negChocoConstraint);
//        }
//
//        Constraint constraint = new Constraint(constraintName);
//        ConstraintUtils.addChocoConstraints(constraint, modelKB, startIdx, modelKB.getNbCstrs() - 1, hasNegativeConstraints);
////        constraint.addChocoConstraints(modelKB, startIdx, modelKB.getNbCstrs() - 1, hasNegativeConstraints);
//        constraintList.add(constraint);
//
//        if (hasNegativeConstraints && negChocoConstraint != null) {
//            modelKB.unpost(negChocoConstraint);
//        }
//    }

    @Override
    public IntVar[] getIntVars() {
        org.chocosolver.solver.variables.Variable[] vars = getModelKB().getVars();

        return Arrays.stream(vars).map(v -> (IntVar) v).toArray(IntVar[]::new);
    }

    @Override
    public IntVar getIntVar(@NonNull String variable) {
        Variable var = getVariable(variable);

        return ((IntVariable) var).getChocoVar();
    }

    // Choco value
    @Override
    public int getIntValue(@NonNull String var, @NonNull String value) {
        Domain domain = getDomain(var);

        return domain.getChocoValue(value);
    }
}
