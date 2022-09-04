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
import at.tugraz.ist.ase.fm.builder.RelationshipBuilder;
import at.tugraz.ist.ase.fm.core.ast.ASTBuilder;
import at.tugraz.ist.ase.fm.core.ast.ASTNode;
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

    @BeforeAll
    static void setUp() {
        fm = new FeatureModel<>("test", new FeatureBuilder(), new RelationshipBuilder(), new ConstraintBuilder());

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
        ASTNode ast1 = ASTBuilder.buildRequires(ABtesting, statistics);
        ASTNode ast2 = ASTBuilder.buildExcludes(ABtesting, nonlicense);
        ASTNode ast3 = ASTBuilder.buildRequires(ABtesting, root);

        CTConstraint c1 = new CTConstraint(ast1);
        assertEquals("requires(ABtesting, statistics)", c1.toString());

        CTConstraint c2 = new CTConstraint(ast2);
        assertEquals("excludes(ABtesting, nonlicense)", c2.toString());

        CTConstraint c3 = new CTConstraint(ast3);
        assertEquals("requires(ABtesting, survey)", c3.toString());

        assertEquals(List.of(ABtesting, statistics), c1.getFeatures());
        assertEquals(List.of(ABtesting, nonlicense), c2.getFeatures());
        assertEquals(List.of(ABtesting, root), c3.getFeatures());
    }

    @Test
    void test2() {
        ASTNode ast1 = ASTBuilder.buildNot(ABtesting);
        ASTNode ast2 = ASTBuilder.buildOr(ast1, statistics);

        CTConstraint c1 = new CTConstraint(ast2);
        assertEquals("~ABtesting \\/ statistics", c1.toString());

        assertEquals(List.of(ABtesting, statistics), c1.getFeatures());
    }

    @Test
    void test3() {
        ASTNode ast1 = ASTBuilder.buildNot(ABtesting);
        ASTNode ast2 = ASTBuilder.buildOr(ast1, statistics);
        ASTNode ast3 = ASTBuilder.buildOr(ast2, singlechoice);

        CTConstraint c1 = new CTConstraint(ast3);
        assertEquals("~ABtesting \\/ statistics \\/ singlechoice", c1.toString());

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
        ASTNode ast1 = ASTBuilder.buildNot(ABtesting);
        ASTNode ast2 = ASTBuilder.buildImplies(pay, ast1);

        CTConstraint c1 = new CTConstraint(ast2);
        assertEquals("pay -> ~ABtesting", c1.toString());

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
        ASTNode ast1 = ASTBuilder.buildAnd(ABtesting, pay);
        ASTNode ast2 = ASTBuilder.buildNot(ast1);

        CTConstraint c1 = new CTConstraint(ast2);
        assertEquals("~(ABtesting /\\ pay)", c1.toString());

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
        ASTNode ast1 = ASTBuilder.buildNot(ABtesting);
        ASTNode ast2 = ASTBuilder.buildOr(pay, ast1);

        CTConstraint c1 = new CTConstraint(ast2);
        assertEquals("pay \\/ ~ABtesting", c1.toString());

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
        ASTNode ast1 = ASTBuilder.buildAnd(ABtesting, pay);
        ASTNode ast2 = ASTBuilder.buildNot(ast1);
        ASTNode ast3 = ASTBuilder.buildImplies(qa, ast2);

        CTConstraint c1 = new CTConstraint(ast3);
        assertEquals("qa -> ~(ABtesting /\\ pay)", c1.toString());

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
        ASTNode ast1 = ASTBuilder.buildNot(ABtesting);
        ASTNode ast2 = ASTBuilder.buildOr(pay, ast1);
        ASTNode ast3 = ASTBuilder.buildImplies(qa, ast2);

        CTConstraint c1 = new CTConstraint(ast3);
        assertEquals("qa -> pay \\/ ~ABtesting", c1.toString());

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
        ASTNode ast1 = ASTBuilder.buildNot(ABtesting);
        ASTNode ast2 = ASTBuilder.buildNot(ast1);
        ASTNode ast3 = ASTBuilder.buildImplies(qa, ast2);

        CTConstraint c1 = new CTConstraint(ast3);
        assertEquals("qa -> ~(~ABtesting)", c1.toString());

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
        ASTNode ast1 = ASTBuilder.buildNot(ABtesting);
        ASTNode ast2 = ASTBuilder.buildNot(ast1);
        ASTNode ast3 = ASTBuilder.buildNot(ast2);
        ASTNode ast4 = ASTBuilder.buildNot(ast3);
        ASTNode ast5 = ASTBuilder.buildImplies(qa, ast4);

        CTConstraint c1 = new CTConstraint(ast5);
        assertEquals("qa -> ~(~(~(~ABtesting)))", c1.toString());

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
        ASTNode ast1 = ASTBuilder.buildNot(ABtesting);
        ASTNode ast2 = ASTBuilder.buildOr(pay, ast1);
        ASTNode ast3 = ASTBuilder.buildImplies(qa, ast2);

        CTConstraint c1 = new CTConstraint(ast3);
        assertEquals("qa -> pay \\/ ~ABtesting", c1.toString());

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
        ASTNode ast1 = ASTBuilder.buildAnd(ABtesting, pay);
        ASTNode ast2 = ASTBuilder.buildEquivalence(qa, ast1);

        CTConstraint c1 = new CTConstraint(ast2);
        assertEquals("qa <-> ABtesting /\\ pay", c1.toString());

        assertEquals(List.of(qa, ABtesting, pay), c1.getFeatures());
    }
}