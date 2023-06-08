/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.core;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

public class Requirement extends Solution {
    @Builder(builderMethodName = "requirementBuilder")
    public Requirement(@NonNull List<Assignment> assignments) {
        super(assignments);
    }
}
