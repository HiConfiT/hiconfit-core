/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represent a 3-CNF constraint.
 * A disjunction of clauses.
 *
 * A \/ B \/ C \/ D \/...
 */
@Getter
public class ThreeCNFConstraint extends Relationship {
    private final List<Clause> clauses;

    /**
     * A constructor for 3CNF constraints.
     * @param type type of constraint
     * @param constraint3CNF a 3CNF constraint.
     */
    @Builder
    public ThreeCNFConstraint(RelationshipType type, @NonNull String constraint3CNF) {
        super(type);

        checkArgument(type == RelationshipType.ThreeCNF, "Relationship type must be 3CNF");

        clauses = new LinkedList<>();
        parse3CNFConstraint(constraint3CNF);

        convertToConfRule();
    }

    @Override
    public boolean contains(@NonNull Feature feature) {
        return clauses.stream().anyMatch(clause -> clause.getLiteral().equals(feature.getName()));
    }

    private void parse3CNFConstraint(String constraint3CNF) {
        String[] clauses = constraint3CNF.split(" \\| ");

        Arrays.stream(clauses).map(Clause::new).forEachOrdered(this.clauses::add);
        /*for (String c: clauses) {
            Clause clause = new Clause(c);
            this.clauses.add(clause);
        }*/
    }

    private void convertToConfRule() {
        confRule = String.format("3cnf(%s)", clauses.stream().map(Clause::getClause).collect(Collectors.joining(", ")));
    }
}
