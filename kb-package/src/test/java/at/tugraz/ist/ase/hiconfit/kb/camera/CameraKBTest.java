/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.camera;

import at.tugraz.ist.ase.hiconfit.kb.core.Variable;
import org.chocosolver.solver.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CameraKBTest {
    static CameraKB kb;

    @BeforeEach
    void setUp() {
        kb = new CameraKB(false);
    }

    @Test
    void testDomains() {
        setUp();

        System.out.println(kb.getConfigurationSize());
    }

    @Test
    void testProduct1() {
        setUp();
        Model model = kb.getModelKB();

        //		208	32	1	1	0	1	0	30	1405	5219
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {208, 32, 1, 1, 0, 1, 0, 30, 1405, 5219};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct2() {
        setUp();
        Model model = kb.getModelKB();

        //		61	25	0	1	0	0	4	30	475	659
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {61, 25, 0, 1, 0, 0, 4, 30, 475, 659};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct3() {
        setUp();
        Model model = kb.getModelKB();

        //		61	18	0	0	0	0	4	20	700	189
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {61, 18, 0, 0, 0, 0, 4, 20, 700, 189};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct4() {
        setUp();
        Model model = kb.getModelKB();

        //		209	32	1	1	1	0	0	58	860	2329
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {209, 32, 1, 1, 1, 0, 0, 58, 860, 2329};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct5() {
        setUp();
        Model model = kb.getModelKB();

        //		243	32	0	0	0	0	2	35	850	1649
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {243, 32, 0, 0, 0, 0, 2, 35, 850, 1649};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct6() {
        setUp();
        Model model = kb.getModelKB();

        //		243	32	0	1	0	0	3	35	840	2149
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {243, 32, 0, 1, 0, 0, 3, 35, 840, 2149};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct7() {
        setUp();
        Model model = kb.getModelKB();

        //		363	32	0	0	0	0	3	50	980	3229
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {363, 32, 0, 0, 0, 0, 3, 50, 980, 3229};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct8() {
        setUp();
        Model model = kb.getModelKB();

        //		102	30	0	0	0	0	4	30	535	400
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {102, 30, 0, 0, 0, 0, 4, 30, 535, 400};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct9() {
        setUp();
        Model model = kb.getModelKB();

        //		142	30	0	0	0	0	1	30	455	469
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {142, 30, 0, 0, 0, 0, 1, 30, 455, 469};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct10() {
        setUp();
        Model model = kb.getModelKB();

        //		242	30	0	0	0	1	2	30	455	581
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {242, 30, 0, 0, 0, 1, 2, 30, 455, 581};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct11() {
        setUp();
        Model model = kb.getModelKB();

        //		242	30	0	0	0	0	3	58	460	399
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {242, 30, 0, 0, 0, 0, 3, 58, 460, 399};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct12() {
        setUp();
        Model model = kb.getModelKB();

        //		242	30	0	0	0	0	3	30	445	499
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {242, 30, 0, 0, 0, 0, 3, 30, 445, 499};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct13() {
        setUp();
        Model model = kb.getModelKB();

        //		123	27	0	0	0	1	5	30	560	579
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {123, 27, 0, 0, 0, 1, 5, 30, 560, 579};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct14() {
        setUp();
        Model model = kb.getModelKB();

        //		162	30	0	0	0	1	2	30	560	469
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {162, 30, 0, 0, 0, 1, 2, 30, 560, 469};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct15() {
        setUp();
        Model model = kb.getModelKB();

        //		241	30	0	1	0	1	2	58	505	479
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {241, 30, 0, 1, 0, 1, 2, 58, 505, 479};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct16() {
        setUp();
        Model model = kb.getModelKB();

        //		242	32	0	1	0	1	3	58	530	609
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {242, 32, 0, 1, 0, 1, 3, 58, 530, 609};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct17() {
        setUp();
        Model model = kb.getModelKB();

        //		242	32	1	1	0	1	3	58	470	749
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {242, 32, 1, 1, 0, 1, 3, 58, 470, 749};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct18() {
        setUp();
        Model model = kb.getModelKB();

        //		241	32	1	1	0	1	2	58	675	669
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {241, 32, 1, 1, 0, 1, 2, 58, 675, 669};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct19() {
        setUp();
        Model model = kb.getModelKB();

        //		242	32	0	1	1	0	3	78	765	1129
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {242, 32, 0, 1, 1, 0, 3, 78, 765, 1129};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }

    @Test
    void testProduct20() {
        setUp();
        Model model = kb.getModelKB();

        //		162	32	0	0	0	0	4	50	765	2749
        List<Variable> vars = kb.getVariableList();
        int[] values = new int[] {162, 32, 0, 0, 0, 0, 4, 50, 765, 2749};
        for (int i = 0; i < vars.size(); i++) {
            model.arithm(kb.getIntVar(vars.get(i).getName()),"=", values[i]).post();
        }

        assertTrue(model.getSolver().solve());
    }
}