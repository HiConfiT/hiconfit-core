/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.configurator;

import at.tugraz.ist.ase.hiconfit.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.IChocoModel;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import at.tugraz.ist.ase.hiconfit.kb.fm.FMKB;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;

import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * rootConstraint - support the root constraint of feature models
 * C = empty
 * B = all constraints
 */
@Slf4j
public class ConfigurationModel extends AbstractCDRModel implements IChocoModel {
    @Getter
    private final Model model;
    private final KB kb;

    @Getter
    private final boolean rootConstraints;

    /**
     * A constructor
     * @param kb a {@link KB}
     * @param rootConstraints - true, a root constraint (f0 = true) will be added to the model to
     *                        make the model always have solutions.
     */
    public ConfigurationModel(@NonNull KB kb, boolean rootConstraints) {
        super(kb.getName());

        if (rootConstraints) {
            checkArgument(kb instanceof FMKB, "Only feature models can have root constraints");
        }

        this.kb = kb;
        this.model = kb.getModelKB();

        this.rootConstraints = rootConstraints;
    }

    @Override
    public void initialize() {
        log.debug("{}Initializing ConfigurationModel for {} >>>", LoggerUtils.tab(), getName());
        LoggerUtils.indent();

        // sets possibly faulty constraints to super class
//        log.trace("{}Adding possibly faulty constraints", LoggerUtils.tab());
//        List<Constraint> C = new LinkedList<>(kb.getConstraintList());
//        if (isReversedConstraintsOrder()) {
//            Collections.reverse(C); // in default, this shouldn't happen
//        }
//        this.setPossiblyFaultyConstraints(C);

        // sets correct constraints to super class
        log.trace("{}Adding correct constraints", LoggerUtils.tab());
        List<Constraint> B = new LinkedList<>(kb.getConstraintList());
        if (isRootConstraints()) { // feature models -> create the root constraint
            // {f0 = true}
//            int startIdx = model.getNbCstrs();
//            String f0 = kb.getVariable(0).getName();
//            BoolVar f0Var = (BoolVar) getVariable(model, f0);
//            model.addClauseTrue(f0Var);
//
//            Constraint constraint = new Constraint(f0 + " = true");
//            constraint.addChocoConstraints(model, startIdx, model.getNbCstrs() - 1, false);

            Constraint constraint = ((FMKB<?, ?, ?>)kb).getRootConstraint();

            B.add(0, constraint);
        }
        this.setCorrectConstraints(B);

        // remove all Choco constraints, cause we just need variables and test cases
        model.unpost(model.getCstrs());

        LoggerUtils.outdent();
        log.debug("{}<<< Model {} initialized", LoggerUtils.tab(), getName());
    }
}
