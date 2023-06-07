///*
// * Consistency-based Algorithms for Conflict Detection and Resolution
// *
// * Copyright (c) 2022
// *
// * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
// */
//
//package at.tugraz.ist.ase.kb.pc;
//
//import at.tugraz.ist.ase.kb.core.Constraint;
//import org.chocosolver.solver.Model;
//import org.chocosolver.solver.exception.ContradictionException;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class SolveErrorTest {
//    static PCKB kb;
//
//    private void check1() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 1: " + model.getCstrs().length);
//
//        for (Constraint constraint : kb.getConstraintList()) {
//            constraint.getChocoConstraints().forEach(model::post);
//        }
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        System.out.println("Sau check 1: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check2() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 2: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        for (Constraint constraint : kb.getConstraintList()) {
//            constraint.getChocoConstraints().forEach(model::post);
//        }
//
//        System.out.println("Sau check 2: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check3() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 3: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 3: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check4() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 4: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(10).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 4: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check5() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 5: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(10).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(4).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(5).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(6).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(7).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(8).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 5: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check6() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 6: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(10).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(4).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(5).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(6).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(7).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(8).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(2).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(3).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 6: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check7() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 7: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(10).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(4).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(5).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(6).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(7).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(8).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(2).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(3).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(1).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 7: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check8() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 8: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(10).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(4).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(5).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(6).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(7).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(8).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(2).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(3).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 8: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check9() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 9: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(10).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(4).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(5).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(6).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(7).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(8).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 9: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check10() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 10: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(10).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 10: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check11() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 11: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 11: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check12() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 12: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 12: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check13() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 13: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 13: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check14() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 14: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(10).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 14: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check15() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 15: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 15: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check16() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 16: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 16: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check17() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 17: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 17: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check18() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 18: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 18: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check19() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 19: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 19: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check20() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 20: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 20: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check21() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 21: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 21: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check22() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 22: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 22: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check23() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 23: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 23: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check24() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 24: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 24: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check25() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 25: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 25: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check26() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 26: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 26: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check27() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 27: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 27: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check28() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 28: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 28: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check29() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 29: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 29: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check30() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 30: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 30: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check31() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 31: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        kb.getConstraint(0).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 31: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    // all conflict sets
//    private void check32() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 32: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        for (int i = 1; i < kb.getNumConstraints(); i++) {
//            kb.getConstraint(i).getChocoConstraints().forEach(model::post);
//        }
//
//        System.out.println("Sau check 32: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check33() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 33: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        for (int i = 0; i < kb.getNumConstraints(); i++) {
//            if (i != 9) {
//                kb.getConstraint(i).getChocoConstraints().forEach(model::post);
//            }
//        }
//
//        System.out.println("Sau check 33: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check34() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 34: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        for (int i = 0; i < kb.getNumConstraints(); i++) {
//            if (i != 17) {
//                kb.getConstraint(i).getChocoConstraints().forEach(model::post);
//            }
//        }
//
//        System.out.println("Sau check 34: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check35() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 35: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        for (int i = 0; i < kb.getNumConstraints(); i++) {
//            if (i != 24) {
//                kb.getConstraint(i).getChocoConstraints().forEach(model::post);
//            }
//        }
//
//        System.out.println("Sau check 35: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check36() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 36: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "200"));
//        model.post(c);
//
//        for (int i = 0; i < kb.getNumConstraints(); i++) {
//            if (i != 33) {
//                kb.getConstraint(i).getChocoConstraints().forEach(model::post);
//            }
//        }
//
//        System.out.println("Sau check 36: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check37() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 37: " + model.getCstrs().length);
//
//        for (Constraint constraint : kb.getConstraintList()) {
//            constraint.getChocoConstraints().forEach(model::post);
//        }
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "400"));
//        model.post(c);
//
//        System.out.println("Sau check 37: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check38() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 38: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "400"));
//        model.post(c);
//
//        for (Constraint constraint : kb.getConstraintList()) {
//            constraint.getChocoConstraints().forEach(model::post);
//        }
//
//        System.out.println("Sau check 38: " + model.getCstrs().length);
//
////        assertFalse(model.getSolver().solve());
//        assertFalse(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check39() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 39: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "400"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 39: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check40() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 40: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "400"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(10).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 40: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    private void check41() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        System.out.println("Truoc check 41: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "400"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(10).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(4).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(5).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(6).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(7).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(8).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 41: " + model.getCstrs().length);
//
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//
//    }
//
//    private void check42() {
//        Model model = kb.getModelKB();
//
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//        model.getSolver().getEngine().flush();
//        System.out.println("Truoc check 42: " + model.getCstrs().length);
//
//        org.chocosolver.solver.constraints.Constraint c = model.arithm(kb.getIntVar("pro_freqD"), "=", kb.getIntValue("pro_freqD", "400"));
//        model.post(c);
//
//        kb.getConstraint(18).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(19).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(20).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(21).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(22).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(23).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(24).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(25).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(26).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(27).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(28).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(29).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(30).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(31).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(32).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(33).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(34).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(35).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(9).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(10).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(11).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(12).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(13).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(14).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(15).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(16).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(17).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(4).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(5).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(6).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(7).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(8).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(2).getChocoConstraints().forEach(model::post);
//        kb.getConstraint(3).getChocoConstraints().forEach(model::post);
//
//        System.out.println("Sau check 42: " + model.getCstrs().length);
//
////        model.getSolver().setSearch(minDomLBSearch(kb.getIntVars()));
////        model.getSolver().setSearch(activityBasedSearch(kb.getIntVars()));
////        model.getSolver().limitTime("10s");
////        model.getSolver().reset();
////        model.getSolver().getEngine().flush();
////        assertTrue(model.getSolver().solve());
//        assertTrue(isConsistent());
//        model.getSolver().reset();
//        model.unpost(model.getCstrs());
//    }
//
//    boolean isConsistent() {
//        Model model = kb.getModelKB();
//
//        boolean isConsistent;
//        model.getEnvironment().worldPush();
//        try {
//            model.getSolver().propagate();
//            isConsistent = true;
//        } catch (ContradictionException e) {
//            model.getSolver().getEngine().flush();
//            isConsistent = false;
//        } finally {
//            model.getEnvironment().worldPop();
//            model.getSolver().reset();
//        }
//
//        return isConsistent;
//    }
//
//    @Test
//    void testChocoSolverError() {
//        kb = new PCKB(false);
//        check1();
//        check2();
//        check3();
//        check4();
//        check5();
//        check6();
//        check7();
//        check8();
//        check9();
//        check10();
//        check11();
//        check12();
//        check13();
//        check14();
//        check15();
//        check16();
//        check17();
//        check18();
//        check19();
//        check20();
//        check21();
//        check22();
//        check23();
//        check24();
//        check25();
//        check26();
//        check27();
//        check28();
//        check29();
//        check30();
//        check31();
//        check32(); // all conflicts
//        check33();
//        check34();
//        check35();
//        check36();
//        check37(); // 400
//        check38();
//        check39();
//        check40();
//        check41();
//        check42();
//    }
//}
