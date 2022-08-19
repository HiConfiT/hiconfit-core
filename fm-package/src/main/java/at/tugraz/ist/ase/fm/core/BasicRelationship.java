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

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents one of the following relationships/constraints:
 * + Mandatory
 * + Optional
 * + Or
 * + Alternative
 * + Requires
 * + Excludes
 */
@Getter
public class BasicRelationship extends Relationship {
    private Feature leftSide;
    private List<Feature> rightSide;

    /**
     * A constructor
     * @param type type of relationship/constraint
     * @param leftSide left part of relationship/constraint
     * @param rightSide right part of relationship/constraint
     */
    @Builder
    public BasicRelationship(RelationshipType type, @NonNull Feature leftSide, @NonNull List<Feature> rightSide) {
        super(type);

        checkArgument(type != RelationshipType.ThreeCNF, "Relationship type must not be 3CNF");
        switch (type) {
            case MANDATORY -> checkArgument(rightSide.size() == 1, "Mandatory relationship's right side must have exactly one feature");
            case OPTIONAL -> checkArgument(rightSide.size() == 1, "Optional relationship's right side must have exactly one feature");
            case OR -> checkArgument(rightSide.size() >= 1, "OR relationship's right side must have more than one feature");
            case ALTERNATIVE -> checkArgument(rightSide.size() >= 1, "Alternative relationship's right side must have more than one feature");
            case REQUIRES -> checkArgument(rightSide.size() == 1, "Requires relationship's right side must have exactly one feature");
            case EXCLUDES -> checkArgument(rightSide.size() == 1, "Excludes relationship's right side must have exactly one feature");
        }

        this.leftSide = leftSide;
        this.rightSide = rightSide;

        convertToConfRule();
    }

    /**
     * Checks whether the given {@link Feature} belongs to the left part of the relationship/constraint.
     * @param feature a {@link Feature}
     * @return true if yes, false otherwise.
     */
    @Override
    public boolean presentAtLeftSide(@NonNull Feature feature) {
        return leftSide.equals(feature);
    }

    /**
     * Checks whether the given {@link Feature} belongs to the right part of the relationship/constraint.
     * @param feature a {@link Feature}
     * @return true if yes, false otherwise.
     */
    @Override
    public boolean presentAtRightSide(@NonNull Feature feature) {
        return rightSide.stream().anyMatch(f -> f.equals(feature));
    }

    private void convertToConfRule() {
        switch (type) {
            case MANDATORY -> confRule = String.format("mandatory(%s, %s)", leftSide, rightSide.get(0));
            case OPTIONAL -> confRule = String.format("optional(%s, %s)", leftSide, rightSide.get(0));
            case REQUIRES -> confRule = String.format("requires(%s, %s)", leftSide, rightSide.get(0));
            case ALTERNATIVE -> confRule = String.format("alternative(%s, %s)", leftSide, rightSide.stream().map(Feature::getName).collect(Collectors.joining(", ")));
            case OR -> confRule = String.format("or(%s, %s)", leftSide, rightSide.stream().map(Feature::getName).collect(Collectors.joining(", ")));
            case EXCLUDES -> confRule = String.format("excludes(%s, %s)", leftSide, rightSide.get(0));
        }
    }

    @Override
    public void dispose() {
        leftSide = null;
        rightSide = null;
    }
}
