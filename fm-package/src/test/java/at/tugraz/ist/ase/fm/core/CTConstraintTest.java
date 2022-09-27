/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.fm.builder.ConstraintBuilder;
import at.tugraz.ist.ase.fm.builder.FeatureBuilder;
import at.tugraz.ist.ase.fm.builder.IConstraintBuildable;
import at.tugraz.ist.ase.fm.builder.RelationshipBuilder;
import at.tugraz.ist.ase.fm.core.ast.ASTBuilder;
import at.tugraz.ist.ase.fm.core.ast.ASTNode;
import at.tugraz.ist.ase.fm.translator.ConfRuleTranslator;
import at.tugraz.ist.ase.fm.translator.IConfRuleTranslatable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CTConstraintTest {
    static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm;
    static Feature root;
    static Feature pay;
    static Feature ABtesting;
    static Feature statistics;
    static Feature qa;
    static Feature license;
    static Feature nonlicense;
    static Feature multiplechoice;
    static Feature singlechoice;

    static IConstraintBuildable constraintBuilder;

    static IConfRuleTranslatable translator = new ConfRuleTranslator();

    @BeforeAll
    static void setUp() {
        constraintBuilder = new ConstraintBuilder(translator);
        fm = new FeatureModel<>("test", new FeatureBuilder(), new RelationshipBuilder(translator), constraintBuilder);

        root = fm.addRoot("survey", "survey");
        // the order of adding features should be breadth-first
        pay = fm.addFeature("pay", "pay");
        ABtesting = fm.addFeature("ABtesting", "ABtesting");
        statistics = fm.addFeature("statistics", "statistics");
        qa = fm.addFeature("qa", "qa");
        license = fm.addFeature("license", "license");
        nonlicense = fm.addFeature("nonlicense", "nonlicense");
        multiplechoice = fm.addFeature("multiplechoice", "multiplechoice");
        singlechoice = fm.addFeature("singlechoice", "singlechoice");

        fm.addMandatoryRelationship(root, pay);
        fm.addOptionalRelationship(root, ABtesting);
        fm.addMandatoryRelationship(root, statistics);
        fm.addMandatoryRelationship(root, qa);
        fm.addAlternativeRelationship(pay, List.of(license, nonlicense));
        fm.addOrRelationship(qa, List.of(multiplechoice, singlechoice));
        fm.addOptionalRelationship(ABtesting, statistics);
    }

    @Test
    void test1() {
        ASTNode ast1 = constraintBuilder.buildRequires(ABtesting, statistics);
        ASTNode ast2 = constraintBuilder.buildExcludes(ABtesting, nonlicense);
        ASTNode ast3 = constraintBuilder.buildRequires(ABtesting, root);

        ASTNode cnf1 = ASTBuilder.convertToCNF(ast1);
        ASTNode cnf2 = ASTBuilder.convertToCNF(ast2);
        ASTNode cnf3 = ASTBuilder.convertToCNF(ast3);

        CTConstraint c1 = new CTConstraint(ast1, cnf1, translator);
        assertEquals("requires(ABtesting, statistics)", c1.toString());
        assertEquals("requires(ABtesting, statistics)", c1.getCnf().toString());

        CTConstraint c2 = new CTConstraint(ast2, cnf2, translator);
        assertEquals("excludes(ABtesting, nonlicense)", c2.toString());
        assertEquals("excludes(ABtesting, nonlicense)", c2.getCnf().toString());

        CTConstraint c3 = new CTConstraint(ast3, cnf3, translator);
        assertEquals("requires(ABtesting, survey)", c3.toString());
        assertEquals("requires(ABtesting, survey)", c3.getCnf().toString());

        assertEquals(List.of(ABtesting, statistics), c1.getFeatures());
        assertEquals(List.of(ABtesting, nonlicense), c2.getFeatures());
        assertEquals(List.of(ABtesting, root), c3.getFeatures());
    }

    @Test
    void test2() {
        ASTNode ast1 = constraintBuilder.buildNot(ABtesting);
        ASTNode ast2 = constraintBuilder.buildOr(ast1, statistics);

        ASTNode cnf = ASTBuilder.convertToCNF(ast2);

        CTConstraint c1 = new CTConstraint(ast2, cnf, translator);
        assertEquals("(~ABtesting \\/ statistics)", c1.toString());
        assertEquals("(~ABtesting \\/ statistics)", c1.getCnf().toString());

        assertEquals(List.of(ABtesting, statistics), c1.getFeatures());
    }

    @Test
    void test3() {
        ASTNode ast1 = constraintBuilder.buildNot(ABtesting);
        ASTNode ast2 = constraintBuilder.buildOr(ast1, statistics);
        ASTNode ast3 = constraintBuilder.buildOr(ast2, singlechoice);

        ASTNode cnf = ASTBuilder.convertToCNF(ast3);

        CTConstraint c1 = new CTConstraint(ast3, cnf, translator);
        assertEquals("((~ABtesting \\/ statistics) \\/ singlechoice)", c1.toString());
        assertEquals("((~ABtesting \\/ statistics) \\/ singlechoice)", c1.getCnf().toString());

        assertEquals(List.of(ABtesting, statistics, singlechoice), c1.getFeatures());
    }

    /**
     * FeatureIDE rule
     * <rule>
     *    <imp>
     *       <var>AB</var>
     *       <not>
     *          <var>CB</var>
     *       </not>
     *    </imp>
     * </rule>
     */
    @Test
    void test4() {
        ASTNode ast1 = constraintBuilder.buildNot(ABtesting);
        ASTNode ast2 = constraintBuilder.buildImplies(pay, ast1);

        ASTNode cnf = ASTBuilder.convertToCNF(ast2);

        CTConstraint c1 = new CTConstraint(ast2, cnf, translator);
        assertEquals("(pay -> ~ABtesting)", c1.toString());
        assertEquals("(~pay \\/ ~ABtesting)", c1.getCnf().toString());

        assertEquals(List.of(pay, ABtesting), c1.getFeatures());
    }

    /**
     * FeatureIDE rule
     * <rule>
     *   <not>
     *       <conj>
     *           <var>ADA</var>
     *           <var>AB</var>
     *       </conj>
     *   </not>
     * </rule>
     */
    @Test
    void test5() {
        ASTNode ast1 = constraintBuilder.buildAnd(ABtesting, pay);
        ASTNode ast2 = constraintBuilder.buildNot(ast1);

        ASTNode cnf = ASTBuilder.convertToCNF(ast2);

        CTConstraint c1 = new CTConstraint(ast2, cnf, translator);
        assertEquals("~(ABtesting /\\ pay)", c1.toString());
        assertEquals("(~ABtesting \\/ ~pay)", c1.getCnf().toString());

        assertEquals(List.of(ABtesting, pay), c1.getFeatures());
    }

    /**
     * FeatureIDE rule
     * <rule>
     *    <disj>
     *        <var>ADB</var>
     *        <not>
     *            <var>BC</var>
     *        </not>
     *    </disj>
     * </rule>
     */
    @Test
    void test6() {
        ASTNode ast1 = constraintBuilder.buildNot(ABtesting);
        ASTNode ast2 = constraintBuilder.buildOr(pay, ast1);

        ASTNode cnf = ASTBuilder.convertToCNF(ast2);

        CTConstraint c1 = new CTConstraint(ast2, cnf, translator);
        assertEquals("(pay \\/ ~ABtesting)", c1.toString());
        assertEquals("(pay \\/ ~ABtesting)", c1.getCnf().toString());

        assertEquals(List.of(pay, ABtesting), c1.getFeatures());
    }

    /**
     * FeatureIDE rule
     * <rule>
     *    <imp>
     *        <var>DAA</var>
     *        <not>
     *            <conj>
     *                <var>AB</var>
     *                <var>AC</var>
     *            </conj>
     *        </not>
     *    </imp>
     * </rule>
     */
    @Test
    void test7() {
        ASTNode ast1 = constraintBuilder.buildAnd(ABtesting, pay);
        ASTNode ast2 = constraintBuilder.buildNot(ast1);
        ASTNode ast3 = constraintBuilder.buildImplies(qa, ast2);

        ASTNode cnf = ASTBuilder.convertToCNF(ast3);

        CTConstraint c1 = new CTConstraint(ast3, cnf, translator);
        assertEquals("(qa -> ~(ABtesting /\\ pay))", c1.toString());
        assertEquals("(~qa \\/ (~ABtesting \\/ ~pay))", c1.getCnf().toString());

        assertEquals(List.of(qa, ABtesting, pay), c1.getFeatures());
    }

    /**
     * FeatureIDE rule
     * <rule>
     *    <imp>
     *        <var>AB</var>
     *        <disj>
     *            <var>AC</var>
     *            <not>
     *                <var>DAB</var>
     *            </not>
     *        </disj>
     *    </imp>
     * </rule>
     */
    @Test
    void test8() {
        ASTNode ast1 = constraintBuilder.buildNot(ABtesting);
        ASTNode ast2 = constraintBuilder.buildOr(pay, ast1);
        ASTNode ast3 = constraintBuilder.buildImplies(qa, ast2);

        ASTNode cnf = ASTBuilder.convertToCNF(ast3);

        CTConstraint c1 = new CTConstraint(ast3, cnf, translator);
        assertEquals("(qa -> (pay \\/ ~ABtesting))", c1.toString());
        assertEquals("(~qa \\/ (pay \\/ ~ABtesting))", c1.getCnf().toString());

        assertEquals(List.of(qa, pay, ABtesting), c1.getFeatures());
    }

    /**
     * FeatureIDE rule
     * <rule>
     *    <imp>
     *        <var>DB</var>
     *        <not>
     *            <not>
     *                <var>AB</var>
     *            </not>
     *        </not>
     *   </imp>
     * </rule>
     */
    @Test
    void test9() {
        ASTNode ast1 = constraintBuilder.buildNot(ABtesting);
        ASTNode ast2 = constraintBuilder.buildNot(ast1);
        ASTNode ast3 = constraintBuilder.buildImplies(qa, ast2);

        ASTNode cnf = ASTBuilder.convertToCNF(ast3);

        CTConstraint c1 = new CTConstraint(ast3, cnf, translator);
        assertEquals("(qa -> ~(~ABtesting))", c1.toString());
        assertEquals("(~qa \\/ ABtesting)", c1.getCnf().toString());

        assertEquals(List.of(qa, ABtesting), c1.getFeatures());
    }

    /**
     * FeatureIDE rule
     * <rule>
     *    <imp>
     *        <var>DB</var>
     *        <not>
     *            <not>
     *                <not>
     *                    <not>
     *                        <var>AC</var>
     *                    </not>
     *                </not>
     *            </not>
     *        </not>
     *    </imp>
     * </rule>
     */
    @Test
    void test10() {
        ASTNode ast1 = constraintBuilder.buildNot(ABtesting);
        ASTNode ast2 = constraintBuilder.buildNot(ast1);
        ASTNode ast3 = constraintBuilder.buildNot(ast2);
        ASTNode ast4 = constraintBuilder.buildNot(ast3);
        ASTNode ast5 = constraintBuilder.buildImplies(qa, ast4);

        ASTNode cnf = ASTBuilder.convertToCNF(ast5);

        CTConstraint c1 = new CTConstraint(ast5, cnf, translator);
        assertEquals("(qa -> ~(~(~(~ABtesting))))", c1.toString());
        assertEquals("(~qa \\/ ABtesting)", c1.getCnf().toString());

        assertEquals(List.of(qa, ABtesting), c1.getFeatures());
    }

    /**
     * FeatureIDE rule
     * <rule>
     *    <imp>
     *        <var>DAA</var>
     *        <disj>
     *            <var>AB</var>
     *            <not>
     *                <var>AC</var>
     *            </not>
     *        </disj>
     *    </imp>
     * </rule>
     */
    @Test
    void test11() {
        ASTNode ast1 = constraintBuilder.buildNot(ABtesting);
        ASTNode ast2 = constraintBuilder.buildOr(pay, ast1);
        ASTNode ast3 = constraintBuilder.buildImplies(qa, ast2);

        ASTNode cnf = ASTBuilder.convertToCNF(ast3);

        CTConstraint c1 = new CTConstraint(ast3, cnf, translator);
        assertEquals("(qa -> (pay \\/ ~ABtesting))", c1.toString());
        assertEquals("(~qa \\/ (pay \\/ ~ABtesting))", c1.getCnf().toString());

        assertEquals(List.of(qa, pay, ABtesting), c1.getFeatures());
    }

    /**
     * FeatureIDE rule
     * <rule>
     *    <eq>
     *        <var>ADA</var>
     *        <conj>
     *            <var>BB</var>
     *            <var>DAB</var>
     *        </conj>
     *    </eq>
     * </rule>
     */
    @Test
    void test12() {
        ASTNode ast1 = constraintBuilder.buildAnd(ABtesting, pay);
        ASTNode ast2 = constraintBuilder.buildEquivalence(qa, ast1);

        ASTNode cnf = ASTBuilder.convertToCNF(ast2);

        CTConstraint c1 = new CTConstraint(ast2, cnf, translator);
        assertEquals("(qa <-> (ABtesting /\\ pay))", c1.toString());
        assertEquals("(((~qa \\/ ABtesting) /\\ (~qa \\/ pay)) /\\ ((~ABtesting \\/ ~pay) \\/ qa))", c1.getCnf().toString());

        assertEquals(List.of(qa, ABtesting, pay), c1.getFeatures());
    }
}