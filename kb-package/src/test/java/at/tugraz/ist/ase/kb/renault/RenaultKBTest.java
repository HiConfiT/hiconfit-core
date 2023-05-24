///*
// * Consistency-based Algorithms for Conflict Detection and Resolution
// *
// * Copyright (c) 2021-2022
// *
// * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
// */
//
//package at.tugraz.ist.ase.kb.renault;
//
//import at.tugraz.ist.ase.kb.core.Constraint;
//import at.tugraz.ist.ase.kb.core.IntVariable;
//import at.tugraz.ist.ase.kb.core.Variable;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RenaultKBTest {
//    static RenaultKB kb;
//
//    @BeforeAll
//    static void init() {
//        kb = new RenaultKB(true);
//    }
//
//    @Test
//    void testConsistency() {
//        assertEquals("KB(name=RenaultConfigurationProblem, source=https://www.itu.dk/research/cla/externals/clib/)", kb.toString());
//        assertTrue(kb.getModelKB().getSolver().solve());
//
////        for (Variable var: kb.getVariableList()) {
////            System.out.println(var.getName() + " = " + var.getChocoValue());
////        }
//
//        kb.getModelKB().getSolver().reset();
//    }
//
//    List<String> expectedDomainList = List.of(
//            "Var1 = [ B64, D64, E64, F64, J64, K25, L64, S64, V25 ]",
//            "Var2 = [ E0, E1, E2, E3, E5 ]",
//            "Var3 = [ M5, M6, M7, M8, M9, MA, MB, MC, MD, ME, MF, MG, MH, MJ, MK, ML, MM, MN, MS, MT, MU, MY, ND1G, NM0C, NM2K ]",
//            "Var4 = [ DIESEL, ESS ]",
//            "Var5 = [ AFSU, ALLE, ARGE, AUST, AUTR, BELG, BRES, CETI, CHIL, COLO, DAIB, DAIC, DAID, DAIF, DANE, DOTO, ESPA, EUOR, FINL, FRAN, GRBR, GREC, HOLL, HONG, IRLA, ISLA, ISRA, ITAL, JAPO, MAGH, MARO, NORV, POLO, PORT, SLVQ, SUED, SUIS, TAIW, TCHE, TURQ, URUG, YOUG ]",
//            "Var6 = [ DD, DG ]",
//            "Var7 = [ GDFROI, TEMP ]",
//            "Var8 = [ CPLG, CPLN ]",
//            "Var9 = [ DA, DM ]",
//            "Var10 = [ SANCOA ]",
//            "Var11 = [ ABS, SSABS ]",
//            "Var12 = [ GALERI, SGALER ]",
//            "Var13 = [ CA, CHAUFO ]",
//            "Var14 = [ CAPVMA, CATOEL, CATOMA, TN, TO, TODEGO ]",
//            "Var15 = [ PBCH, PBNCH ]",
//            "Var16 = [ VC, VT ]",
//            "Var17 = [ SPRTEL ]",
//            "Var18 = [ ELA, SSELA ]",
//            "Var19 = [ CPE, SSCPE ]",
//            "Var20 = [ SSTIR, TIR ]",
//            "Var21 = [ RETROE, RETROR ]",
//            "Var22 = [ REGSIT, SSRSIT ]",
//            "Var23 = [ PROJAB, SPROJA ]",
//            "Var24 = [ SSARCE ]",
//            "Var25 = [ CUSFIX, CUSPIV, SANCUS ]",
//            "Var26 = [ CLB, CLCGRI, SANCL ]",
//            "Var27 = [ ADAC, SADAC ]",
//            "Var28 = [ CCHBAG, SCCHBA ]",
//            "Var29 = [ CUI, DRA ]",
//            "Var30 = [ ATARPH, CRIT3ATRPH, SSATAR ]",
//            "Var31 = [ BANAR, CRIT3SJAR, CRIT3SJARI, FBANAR, FBARAC, SBANAR ]",
//            "Var32 = [ SGMANU, SGMEMO ]",
//            "Var33 = [ KM, MILES ]",
//            "Var34 = [ Autre167, CPK01, CPK02 ]",
//            "Var35 = [ RIDOAR, SRIDAR ]",
//            "Var36 = [ PTCAV ]",
//            "Var39 = [ CATADI, SCATAD ]",
//            "Var40 = [ EMBNOR, EMBPIL, SEMBRY ]",
//            "Var41 = [ PNERFL, PNESTD ]",
//            "Var42 = [ PHAN01, PHAN02, SSPHAN ]",
//            "Var43 = [ ETPN01, ETPN02, SSETPN ]",
//            "Var44 = [ Autre272, EQGPL, PREGPL ]",
//            "Var45 = [ SUSNOR, SUSREN ]",
//            "Var46 = [ RENTC, RETC ]",
//            "Var47 = [ LVAVEL, LVAVMA ]",
//            "Var48 = [ LVAREL, LVARMA, SSLVAR ]",
//            "Var49 = [ Autre310, SSAMVA ]",
//            "Var50 = [ SASURV ]",
//            "Var51 = [ SGACHA, SGSCHA ]",
//            "Var52 = [ COFIXE, COLOMB, COREHA, CORHLO ]",
//            "Var53 = [ AVPFIL, SSPFIL ]",
//            "Var54 = [ NAVIG, SNAVIG ]",
//            "Var55 = [ CRIT2RHENF, RHENF, SRHENF ]",
//            "Var56 = [ APL01, APL02 ]",
//            "Var57 = [ AILAR, SAILAR ]",
//            "Var58 = [ MGMAZE, MGMECO, MGMINI, MGMRNA, MGMRNE, MGMRTA, MGMRTE, MGMRXT, RL, RN, RT, RXE, SMONEQ ]",
//            "Var59 = [ RUNLI, SRUNLI ]",
//            "Var60 = [ LAVPH, SLAVPH ]",
//            "Var61 = [ EVCFIX, EVCVAR ]",
//            "Var62 = [ AOEF, SAOEF ]",
//            "Var63 = [ VOLNRH, VOLRH ]",
//            "Var64 = [ PANP00, PANP01, PANP02, PANP03, PANP05 ]",
//            "Var65 = [ PLAFNT, PLAFT ]",
//            "Var66 = [ JANALU, JANTOL ]",
//            "Var67 = [ T0, T1, T2, T3, T5 ]",
//            "Var68 = [ PSCOMI, PSCOMR, PSCOPO ]",
//            "Var69 = [ PSPAMI, PSPAMR ]",
//            "Var70 = [ ETAP01, ETAP03, SSETAP ]",
//            "Var71 = [ FSTPO ]",
//            "Var72 = [ DUCA, FUJI, KANG, ODIN, PARALL, PARBRE, PARGBR, PARPOL, PARTCH, SSEDNC, VRMI ]",
//            "Var73 = [ TBOR00, TBOR01, TBOR02, TBOR03, TBOR05 ]",
//            "Var74 = [ PBOR00, PBOR01, PBOR02, PBOR03, PBOR05 ]",
//            "Var75 = [ MOCY01, MOCY02, MOCY03, MOCY04, MOCY05, MOCY06, MOCY07, MOCY08, MOCY10, MOCY11, MOCY12, SSMOCY ]",
//            "Var76 = [ Autre408, NINAV1 ]",
//            "Var77 = [ ABPA01, SSABPA ]",
//            "Var78 = [ FIPOU, SFIPOU ]",
//            "Var79 = [ ABCO01, SSABCO ]",
//            "Var80 = [ ANSRAD, CRIT2X5KS, CRIT2X8KI, CRIT4X15CI, CRIT4X15KI, CRIT4X25KI, SRADIO ]",
//            "Var81 = [ BVA4, BVM5 ]",
//            "Var82 = [ NMAS01, NMAS02, NMAS03, NMAS04, NMAS05 ]",
//            "Var83 = [ Autre432, PLARPT, PLARVN, PLARVO ]",
//            "Var84 = [ ECLAR, SECLAR ]",
//            "Var85 = [ CDCOF, SCDCOF ]",
//            "Var86 = [ ACPLAR, SACPLA ]",
//            "Var87 = [ MONORM, SURMO1 ]",
//            "Var88 = [ Autre497, JANDIF ]",
//            "Var89 = [ Autre513, EVA, EVE ]",
//            "Var90 = [ ANTID, ANTIDI, SDPCLV ]",
//            "Var91 = [ Autre613, TKO ]",
//            "Var92 = [ Autre713, CPNT01, CPNT02, CPNT03, EQDIF ]",
//            "Var93 = [ Autre714, BVDIF ]",
//            "Var94 = [ CRIT1503, EU00, EU93, EU96 ]",
//            "Var95 = [ CRIT1149CC, CRIT1390CC, CRIT1598CC, CRIT1870CC, CRIT1998CC ]",
//            "Var96 = [ CRIT060CV, CRIT065CV, CRIT070CV, CRIT075CV, CRIT090CV, CRIT095CV, CRIT100CV, CRIT105CV, CRIT115CV, CRIT150CV ]",
//            "Var97 = [ BCNTC, BCTC ]",
//            "Var98 = [ AZE, Autre913, CPTECO ]",
//            "Var99 = [ AD4, DP0, JB1, JB3, JC5 ]",
//            "Var100 = [ CRIT620, CRIT622, CRIT624, CRIT700, CRIT701, CRIT702, CRIT703, CRIT710, CRIT714, CRIT720, CRIT730, CRIT731, CRIT732, CRIT733, CRIT734, CRIT736, CRIT740, CRIT750, CRIT751, CRIT752, CRIT764, CRIT784, CRIT786, CRIT788, CRIT790, CRIT791, CRIT796, CRIT797, CRIT798 ]",
//            "Var101 = [ D7F, E7J, F3R, F4R, F7R, F8Q, F9Q, K4J, K4M, K7M ]"
//    );
//
//    @Test
//    void testVariables() {
//        assertAll(() -> assertEquals(99, kb.getNumVariables()),
//                () -> {
//                    for (int i = 0; i < kb.getNumVariables(); i++) {
//                        Variable var = kb.getVariable(i);
//                        assertTrue(var instanceof IntVariable);
//                        assertEquals(expectedDomainList.get(i), var.getDomain().toString());
//                    }
//                },
//                () -> assertEquals(3178088, kb.getModelKB().getNbVars()));
//    }
//
//    @Test
//    public void testDefineConstraints() {
//        assertAll(() -> assertEquals(1694394, kb.getModelKB().getNbCstrs()),
//                () -> assertEquals(113, kb.getNumConstraints()),
//                () -> {
//                    for (Constraint constraint : kb.getConstraintList()) {
//                        assertEquals(constraint.getChocoConstraints().size(), constraint.getNegChocoConstraints().size());
//                    }
//                },
//                () -> {
//                    for (Constraint constraint : kb.getConstraintList()) {
//                        for (int i = 0; i < constraint.getChocoConstraints().size() - 1; i++) {
//                            assertEquals(constraint.getChocoConstraints().get(i).toString(), constraint.getNegChocoConstraints().get(i).toString());
//                        }
//                        int lastIndex = constraint.getChocoConstraints().size() - 1;
//                        assertNotEquals(constraint.getChocoConstraints().get(lastIndex).toString(), constraint.getNegChocoConstraints().get(lastIndex).toString());
//                    }
//                });
//    }
//
////    @Test
////    void testSmallRenaults() {
////        assertEquals(113, kb.getNumConstraints());
////
////        kb.getConstraintList().forEach(c -> {
////            System.out.println(c);
////            System.out.println("CONSTRAINT:");
////            c.getChocoConstraints().forEach(System.out::println);
////            System.out.println("NEGATIVE CONSTRAINT:");
////            c.getNegChocoConstraints().forEach(System.out::println);
////        });
////    }
//
////    @Test
////    void testContains() {
////        for (org.chocosolver.solver.constraints.Constraint cstr : kb.getModelKB().getCstrs()) {
////            boolean found = false;
////            for (at.tugraz.ist.ase.knowledgebases.core.Constraint constraint : kb.getConstraintList()) {
////                if (constraint.contains(cstr)) {
////                    found = true;
////                    break;
////                }
////            }
////
////            assertTrue(found, "Constraint " + cstr + " not found in any constraint");
////        }
////    }
//
//    @Test
//    void shouldEmptyNegConstraints() {
//        kb.reset(false);
//        for (Constraint constraint : kb.getConstraintList()) {
//            assertEquals(0, constraint.getNegChocoConstraints().size());
//            assertTrue(constraint.getNegChocoConstraints().isEmpty());
//        }
//    }
//}