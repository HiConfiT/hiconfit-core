/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.renault;

import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.*;
import at.tugraz.ist.ase.hiconfit.kb.core.builder.IntVarConstraintBuilder;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static at.tugraz.ist.ase.hiconfit.common.IOUtils.getInputStream;

@Slf4j
public class RenaultKB extends KB implements IIntVarKB {

    public RenaultKB(boolean hasNegativeConstraints) {
        super("RenaultConfigurationProblem", "https://www.itu.dk/research/cla/externals/clib/", hasNegativeConstraints);

        reset(hasNegativeConstraints);
    }

    @Override
    public void reset(boolean hasNegativeConstraints) {
        log.trace("{}Creating RenaultKB >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        modelKB = new Model(name);
        variableList = new LinkedList<>();
        domainList = new LinkedList<>();
        constraintList = new LinkedList<>();
        defineDomains();
        defineVariables();
        defineConstraints(hasNegativeConstraints);

        // TODO - implement notKB

        LoggerUtils.outdent();
        log.debug("{}<<< Created RenaultKB", LoggerUtils.tab());
    }

    List<String> ruleFiles = List.of("001.pm", "002.pm", "003.pm", "004.pm", "005.pm", "006.pm", "007.pm", "008.pm", "009.pm", "010.pm",
            "011.pm", "012.pm", "013.pm", "014.pm", "015.pm", "016.pm", "017.pm", "018.pm", "019.pm", "020.pm",
            "021.pm", "022.pm", "023.pm", "024.pm", "025.pm", "026.pm", "027.pm", "028.pm", "029.pm", "030.pm",
            "031.pm", "032.pm", "033.pm", "034.pm", "035.pm", "036.pm", "037.pm", "038.pm", "039.pm", "040.pm",
            "041.pm", "042.pm", "043.pm", "044.pm", "045.pm", "046.pm", "047.pm", "048.pm", "049.pm", "050.pm",
            "051.pm", "052.pm", "053.pm", "054.pm", "055.pm", "056.pm", "057.pm", "058.pm", "059.pm", "060.pm",
            "061.pm", "062.pm", "063.pm", "064.pm", "065.pm", "066.pm", "067.pm", "068.pm", "069.pm", "070.pm",
            "071.pm", "072.pm", "073.pm", "074.pm", "075.pm", "076.pm", "077.pm", "078.pm", "079.pm", "080.pm",
            "081.pm", "082.pm", "083.pm", "084.pm", "085.pm", "086.pm", "087.pm", "088.pm", "089.pm", "090.pm",
            "091.pm", "092.pm", "093.pm", "094.pm", "095.pm", "096.pm", "097.pm", "098.pm", "099.pm", "100.pm",
            "101.pm", "102.pm", "103.pm", "104.pm", "105.pm", "106.pm", "107.pm", "108.pm", "109.pm", "110.pm",
            "111.pm", "112.pm", "113.pm");

    private void defineDomains() {
        log.trace("{}Creating domains >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        // 1
        // Var1 : B64, D64, E64, F64, J64, K25, L64, S64, V25
        domainList.add(Domain.builder()
                .name("Var1")
                .values(List.of("B64", "D64", "E64", "F64", "J64", "K25", "L64", "S64", "V25"))
                .build());
        // 2
        // Var2 : E0, E1, E2, E3, E5
        domainList.add(Domain.builder()
                .name("Var2")
                .values(List.of("E0", "E1", "E2", "E3", "E5"))
                .build());
        // 3
        // Var3 : M5, M6, M7, M8, M9, MA, MB, MC, MD, ME, MF, MG, MH, MJ, MK, ML, MM, MN, MS, MT, MU, MY, ND1G, NM0C, NM2K
        domainList.add(Domain.builder()
                .name("Var3")
                .values(List.of("M5", "M6", "M7", "M8", "M9", "MA", "MB", "MC", "MD", "ME", "MF", "MG", "MH", "MJ", "MK", "ML", "MM", "MN", "MS", "MT", "MU", "MY", "ND1G", "NM0C", "NM2K"))
                .build());
        // 4
        // Var4 : DIESEL, ESS
        domainList.add(Domain.builder()
                .name("Var4")
                .values(List.of("DIESEL", "ESS"))
                .build());
        // 5
        // Var5 : AFSU, ALLE, ARGE, AUST, AUTR, BELG, BRES, CETI, CHIL, COLO, DAIB, DAIC, DAID, DAIF, DANE, DOTO, ESPA, EUOR, FINL, FRAN, GRBR, GREC, HOLL, HONG, IRLA, ISLA, ISRA, ITAL, JAPO, MAGH, MARO, NORV, POLO, PORT, SLVQ, SUED, SUIS, TAIW, TCHE, TURQ, URUG, YOUG
        domainList.add(Domain.builder()
                .name("Var5")
                .values(List.of("AFSU", "ALLE", "ARGE", "AUST", "AUTR", "BELG", "BRES", "CETI", "CHIL", "COLO", "DAIB", "DAIC", "DAID", "DAIF", "DANE", "DOTO", "ESPA", "EUOR", "FINL", "FRAN", "GRBR", "GREC", "HOLL", "HONG", "IRLA", "ISLA", "ISRA", "ITAL", "JAPO", "MAGH", "MARO", "NORV", "POLO", "PORT", "SLVQ", "SUED", "SUIS", "TAIW", "TCHE", "TURQ", "URUG", "YOUG"))
                .build());
        // 6
        // Var6 : DD, DG
        domainList.add(Domain.builder()
                .name("Var6")
                .values(List.of("DD", "DG"))
                .build());
        // 7
        // Var7 : GDFROI, TEMP
        domainList.add(Domain.builder()
                .name("Var7")
                .values(List.of("GDFROI", "TEMP"))
                .build());
        // 8
        // Var8 : CPLG, CPLN
        domainList.add(Domain.builder()
                .name("Var8")
                .values(List.of("CPLG", "CPLN"))
                .build());
        // 9
        // Var9 : DA, DM
        domainList.add(Domain.builder()
                .name("Var9")
                .values(List.of("DA", "DM"))
                .build());
        // 10
        // Var10 : SANCOA
        domainList.add(Domain.builder()
                .name("Var10")
                .values(List.of("SANCOA"))
                .build());
        // 11
        // Var11 : ABS, SSABS
        domainList.add(Domain.builder()
                .name("Var11")
                .values(List.of("ABS", "SSABS"))
                .build());
        // 12
        // Var12 : GALERI, SGALER
        domainList.add(Domain.builder()
                .name("Var12")
                .values(List.of("GALERI", "SGALER"))
                .build());
        // 13
        // Var13 : CA, CHAUFO
        domainList.add(Domain.builder()
                .name("Var13")
                .values(List.of("CA", "CHAUFO"))
                .build());
        // 14
        // Var14 : CAPVMA, CATOEL, CATOMA, TN, TO, TODEGO
        domainList.add(Domain.builder()
                .name("Var14")
                .values(List.of("CAPVMA", "CATOEL", "CATOMA", "TN", "TO", "TODEGO"))
                .build());
        // 15
        // Var15 : PBCH, PBNCH
        domainList.add(Domain.builder()
                .name("Var15")
                .values(List.of("PBCH", "PBNCH"))
                .build());
        // 16
        // Var16 : VC, VT
        domainList.add(Domain.builder()
                .name("Var16")
                .values(List.of("VC", "VT"))
                .build());
        // 17
        // Var17 : SPRTEL
        domainList.add(Domain.builder()
                .name("Var17")
                .values(List.of("SPRTEL"))
                .build());
        // 18
        // Var18 : ELA, SSELA
        domainList.add(Domain.builder()
                .name("Var18")
                .values(List.of("ELA", "SSELA"))
                .build());
        // 19
        // Var19 : CPE, SSCPE
        domainList.add(Domain.builder()
                .name("Var19")
                .values(List.of("CPE", "SSCPE"))
                .build());
        // 20
        // Var20 : SSTIR, TIR
        domainList.add(Domain.builder()
                .name("Var20")
                .values(List.of("SSTIR", "TIR"))
                .build());
        // 21
        // Var21 : RETROE, RETROR
        domainList.add(Domain.builder()
                .name("Var21")
                .values(List.of("RETROE", "RETROR"))
                .build());
        // 22
        // Var22 : REGSIT, SSRSIT
        domainList.add(Domain.builder()
                .name("Var22")
                .values(List.of("REGSIT", "SSRSIT"))
                .build());
        // 23
        // Var23 : PROJAB, SPROJA
        domainList.add(Domain.builder()
                .name("Var23")
                .values(List.of("PROJAB", "SPROJA"))
                .build());
        // 24
        // Var24 : SSARCE
        domainList.add(Domain.builder()
                .name("Var24")
                .values(List.of("SSARCE"))
                .build());
        // 25
        // Var25 : CUSFIX, CUSPIV, SANCUS
        domainList.add(Domain.builder()
                .name("Var25")
                .values(List.of("CUSFIX", "CUSPIV", "SANCUS"))
                .build());
        // 26
        // Var26 : CLB, CLCGRI, SANCL
        domainList.add(Domain.builder()
                .name("Var26")
                .values(List.of("CLB", "CLCGRI", "SANCL"))
                .build());
        // 27
        // Var27 : ADAC, SADAC
        domainList.add(Domain.builder()
                .name("Var27")
                .values(List.of("ADAC", "SADAC"))
                .build());
        // 28
        // Var28 : CCHBAG, SCCHBA
        domainList.add(Domain.builder()
                .name("Var28")
                .values(List.of("CCHBAG", "SCCHBA"))
                .build());
        // 29
        // Var29 : CUI, DRA
        domainList.add(Domain.builder()
                .name("Var29")
                .values(List.of("CUI", "DRA"))
                .build());
        // 30
        // Var30 : ATARPH, CRIT3ATRPH, SSATAR
        domainList.add(Domain.builder()
                .name("Var30")
                .values(List.of("ATARPH", "CRIT3ATRPH", "SSATAR"))
                .build());
        // 31
        // Var31 : BANAR, CRIT3SJAR, CRIT3SJARI, FBANAR, FBARAC, SBANAR
        domainList.add(Domain.builder()
                .name("Var31")
                .values(List.of("BANAR", "CRIT3SJAR", "CRIT3SJARI", "FBANAR", "FBARAC", "SBANAR"))
                .build());
        // 32
        // Var32 : SGMANU, SGMEMO
        domainList.add(Domain.builder()
                .name("Var32")
                .values(List.of("SGMANU", "SGMEMO"))
                .build());
        // 33
        // Var33 : KM, MILES
        domainList.add(Domain.builder()
                .name("Var33")
                .values(List.of("KM", "MILES"))
                .build());
        // 34
        // Var34 : Autre167, CPK01, CPK02
        domainList.add(Domain.builder()
                .name("Var34")
                .values(List.of("Autre167", "CPK01", "CPK02"))
                .build());
        // 35
        // Var35 : RIDOAR, SRIDAR
        domainList.add(Domain.builder()
                .name("Var35")
                .values(List.of("RIDOAR", "SRIDAR"))
                .build());
        // 36
        // Var36 : PTCAV
        domainList.add(Domain.builder()
                .name("Var36")
                .values(List.of("PTCAV"))
                .build());
        // 37
        // Var39 : CATADI, SCATAD
        domainList.add(Domain.builder()
                .name("Var39")
                .values(List.of("CATADI", "SCATAD"))
                .build());
        // 38
        // Var40 : EMBNOR, EMBPIL, SEMBRY
        domainList.add(Domain.builder()
                .name("Var40")
                .values(List.of("EMBNOR", "EMBPIL", "SEMBRY"))
                .build());
        // 39
        // Var41 : PNERFL, PNESTD
        domainList.add(Domain.builder()
                .name("Var41")
                .values(List.of("PNERFL", "PNESTD"))
                .build());
        // 40
        // Var42 : PHAN01, PHAN02, SSPHAN
        domainList.add(Domain.builder()
                .name("Var42")
                .values(List.of("PHAN01", "PHAN02", "SSPHAN"))
                .build());
        // 41
        // Var43 : ETPN01, ETPN02, SSETPN
        domainList.add(Domain.builder()
                .name("Var43")
                .values(List.of("ETPN01", "ETPN02", "SSETPN"))
                .build());
        // 42
        // Var44 : Autre272, EQGPL, PREGPL
        domainList.add(Domain.builder()
                .name("Var44")
                .values(List.of("Autre272", "EQGPL", "PREGPL"))
                .build());
        // 43
        // Var45 : SUSNOR, SUSREN
        domainList.add(Domain.builder()
                .name("Var45")
                .values(List.of("SUSNOR", "SUSREN"))
                .build());
        // 44
        // Var46 : RENTC, RETC
        domainList.add(Domain.builder()
                .name("Var46")
                .values(List.of("RENTC", "RETC"))
                .build());
        // 45
        // Var47 : LVAVEL, LVAVMA
        domainList.add(Domain.builder()
                .name("Var47")
                .values(List.of("LVAVEL", "LVAVMA"))
                .build());
        // 46
        // Var48 : LVAREL, LVARMA, SSLVAR
        domainList.add(Domain.builder()
                .name("Var48")
                .values(List.of("LVAREL", "LVARMA", "SSLVAR"))
                .build());
        // 47
        // Var49 : Autre310, SSAMVA
        domainList.add(Domain.builder()
                .name("Var49")
                .values(List.of("Autre310", "SSAMVA"))
                .build());
        // 48
        // Var50 : SASURV
        domainList.add(Domain.builder()
                .name("Var50")
                .values(List.of("SASURV"))
                .build());
        // 49
        // Var51 : SGACHA, SGSCHA
        domainList.add(Domain.builder()
                .name("Var51")
                .values(List.of("SGACHA", "SGSCHA"))
                .build());
        // 50
        // Var52 : COFIXE, COLOMB, COREHA, CORHLO
        domainList.add(Domain.builder()
                .name("Var52")
                .values(List.of("COFIXE", "COLOMB", "COREHA", "CORHLO"))
                .build());
        // 51
        // Var53 : AVPFIL, SSPFIL
        domainList.add(Domain.builder()
                .name("Var53")
                .values(List.of("AVPFIL", "SSPFIL"))
                .build());
        // 52
        // Var54 : NAVIG, SNAVIG
        domainList.add(Domain.builder()
                .name("Var54")
                .values(List.of("NAVIG", "SNAVIG"))
                .build());
        // 53
        // Var55 : CRIT2RHENF, RHENF, SRHENF
        domainList.add(Domain.builder()
                .name("Var55")
                .values(List.of("CRIT2RHENF", "RHENF", "SRHENF"))
                .build());
        // 54
        // Var56 : APL01, APL02
        domainList.add(Domain.builder()
                .name("Var56")
                .values(List.of("APL01", "APL02"))
                .build());
        // 55
        // Var57 : AILAR, SAILAR
        domainList.add(Domain.builder()
                .name("Var57")
                .values(List.of("AILAR", "SAILAR"))
                .build());
        // 56
        // Var58 : MGMAZE, MGMECO, MGMINI, MGMRNA, MGMRNE, MGMRTA, MGMRTE, MGMRXT, RL, RN, RT, RXE, SMONEQ
        domainList.add(Domain.builder()
                .name("Var58")
                .values(List.of("MGMAZE", "MGMECO", "MGMINI", "MGMRNA", "MGMRNE", "MGMRTA", "MGMRTE", "MGMRXT", "RL", "RN", "RT", "RXE", "SMONEQ"))
                .build());
        // 57
        // Var59 : RUNLI, SRUNLI
        domainList.add(Domain.builder()
                .name("Var59")
                .values(List.of("RUNLI", "SRUNLI"))
                .build());
        // 58
        // Var60 : LAVPH, SLAVPH
        domainList.add(Domain.builder()
                .name("Var60")
                .values(List.of("LAVPH", "SLAVPH"))
                .build());
        // 59
        // Var61 : EVCFIX, EVCVAR
        domainList.add(Domain.builder()
                .name("Var61")
                .values(List.of("EVCFIX", "EVCVAR"))
                .build());
        // 60
        // Var62 : AOEF, SAOEF
        domainList.add(Domain.builder()
                .name("Var62")
                .values(List.of("AOEF", "SAOEF"))
                .build());
        // 61
        // Var63 : VOLNRH, VOLRH
        domainList.add(Domain.builder()
                .name("Var63")
                .values(List.of("VOLNRH", "VOLRH"))
                .build());
        // 62
        // Var64 : PANP00, PANP01, PANP02, PANP03, PANP05
        domainList.add(Domain.builder()
                .name("Var64")
                .values(List.of("PANP00", "PANP01", "PANP02", "PANP03", "PANP05"))
                .build());
        // 63
        // Var65 : PLAFNT, PLAFT
        domainList.add(Domain.builder()
                .name("Var65")
                .values(List.of("PLAFNT", "PLAFT"))
                .build());
        // 64
        // Var66 : JANALU, JANTOL
        domainList.add(Domain.builder()
                .name("Var66")
                .values(List.of("JANALU", "JANTOL"))
                .build());
        // 65
        // Var67 : T0, T1, T2, T3, T5
        domainList.add(Domain.builder()
                .name("Var67")
                .values(List.of("T0", "T1", "T2", "T3", "T5"))
                .build());
        // 66
        // Var68 : PSCOMI, PSCOMR, PSCOPO
        domainList.add(Domain.builder()
                .name("Var68")
                .values(List.of("PSCOMI", "PSCOMR", "PSCOPO"))
                .build());
        // 67
        // Var69 : PSPAMI, PSPAMR
        domainList.add(Domain.builder()
                .name("Var69")
                .values(List.of("PSPAMI", "PSPAMR"))
                .build());
        // 68
        // Var70 : ETAP01, ETAP03, SSETAP
        domainList.add(Domain.builder()
                .name("Var70")
                .values(List.of("ETAP01", "ETAP03", "SSETAP"))
                .build());
        // 69
        // Var71 : FSTPO
        domainList.add(Domain.builder()
                .name("Var71")
                .values(List.of("FSTPO"))
                .build());
        // 70
        // Var72 : DUCA, FUJI, KANG, ODIN, PARALL, PARBRE, PARGBR, PARPOL, PARTCH, SSEDNC, VRMI
        domainList.add(Domain.builder()
                .name("Var72")
                .values(List.of("DUCA", "FUJI", "KANG", "ODIN", "PARALL", "PARBRE", "PARGBR", "PARPOL", "PARTCH", "SSEDNC", "VRMI"))
                .build());
        // 71
        // Var73 : TBOR00, TBOR01, TBOR02, TBOR03, TBOR05
        domainList.add(Domain.builder()
                .name("Var73")
                .values(List.of("TBOR00", "TBOR01", "TBOR02", "TBOR03", "TBOR05"))
                .build());
        // 72
        // Var74 : PBOR00, PBOR01, PBOR02, PBOR03, PBOR05
        domainList.add(Domain.builder()
                .name("Var74")
                .values(List.of("PBOR00", "PBOR01", "PBOR02", "PBOR03", "PBOR05"))
                .build());
        // 73
        // Var75 : MOCY01, MOCY02, MOCY03, MOCY04, MOCY05, MOCY06, MOCY07, MOCY08, MOCY10, MOCY11, MOCY12, SSMOCY
        domainList.add(Domain.builder()
                .name("Var75")
                .values(List.of("MOCY01", "MOCY02", "MOCY03", "MOCY04", "MOCY05", "MOCY06", "MOCY07", "MOCY08", "MOCY10", "MOCY11", "MOCY12", "SSMOCY"))
                .build());
        // 74
        // Var76 : Autre408, NINAV1
        domainList.add(Domain.builder()
                .name("Var76")
                .values(List.of("Autre408", "NINAV1"))
                .build());
        // 75
        // Var77 : ABPA01, SSABPA
        domainList.add(Domain.builder()
                .name("Var77")
                .values(List.of("ABPA01", "SSABPA"))
                .build());
        // 76
        // Var78 : FIPOU, SFIPOU
        domainList.add(Domain.builder()
                .name("Var78")
                .values(List.of("FIPOU", "SFIPOU"))
                .build());
        // 77
        // Var79 : ABCO01, SSABCO
        domainList.add(Domain.builder()
                .name("Var79")
                .values(List.of("ABCO01", "SSABCO"))
                .build());
        // 78
        // Var80 : ANSRAD, CRIT2X5KS, CRIT2X8KI, CRIT4X15CI, CRIT4X15KI, CRIT4X25KI, SRADIO
        domainList.add(Domain.builder()
                .name("Var80")
                .values(List.of("ANSRAD", "CRIT2X5KS", "CRIT2X8KI", "CRIT4X15CI", "CRIT4X15KI", "CRIT4X25KI", "SRADIO"))
                .build());
        // 79
        // Var81 : BVA4, BVM5
        domainList.add(Domain.builder()
                .name("Var81")
                .values(List.of("BVA4", "BVM5"))
                .build());
        // 80
        // Var82 : NMAS01, NMAS02, NMAS03, NMAS04, NMAS05
        domainList.add(Domain.builder()
                .name("Var82")
                .values(List.of("NMAS01", "NMAS02", "NMAS03", "NMAS04", "NMAS05"))
                .build());
        // 81
        // Var83 : Autre432, PLARPT, PLARVN, PLARVO
        domainList.add(Domain.builder()
                .name("Var83")
                .values(List.of("Autre432", "PLARPT", "PLARVN", "PLARVO"))
                .build());
        // 82
        // Var84 : ECLAR, SECLAR
        domainList.add(Domain.builder()
                .name("Var84")
                .values(List.of("ECLAR", "SECLAR"))
                .build());
        // 83
        // Var85 : CDCOF, SCDCOF
        domainList.add(Domain.builder()
                .name("Var85")
                .values(List.of("CDCOF", "SCDCOF"))
                .build());
        // 84
        // Var86 : ACPLAR, SACPLA
        domainList.add(Domain.builder()
                .name("Var86")
                .values(List.of("ACPLAR", "SACPLA"))
                .build());
        // 85
        // Var87 : MONORM, SURMO1
        domainList.add(Domain.builder()
                .name("Var87")
                .values(List.of("MONORM", "SURMO1"))
                .build());
        // 86
        // Var88 : Autre497, JANDIF
        domainList.add(Domain.builder()
                .name("Var88")
                .values(List.of("Autre497", "JANDIF"))
                .build());
        // 87
        // Var89 : Autre513, EVA, EVE
        domainList.add(Domain.builder()
                .name("Var89")
                .values(List.of("Autre513", "EVA", "EVE"))
                .build());
        // 88
        // Var90 : ANTID, ANTIDI, SDPCLV
        domainList.add(Domain.builder()
                .name("Var90")
                .values(List.of("ANTID", "ANTIDI", "SDPCLV"))
                .build());
        // 89
        // Var91 : Autre613, TKO
        domainList.add(Domain.builder()
                .name("Var91")
                .values(List.of("Autre613", "TKO"))
                .build());
        // 90
        // Var92 : Autre713, CPNT01, CPNT02, CPNT03, EQDIF
        domainList.add(Domain.builder()
                .name("Var92")
                .values(List.of("Autre713", "CPNT01", "CPNT02", "CPNT03", "EQDIF"))
                .build());
        // 91
        // Var93 : Autre714, BVDIF
        domainList.add(Domain.builder()
                .name("Var93")
                .values(List.of("Autre714", "BVDIF"))
                .build());
        // 92
        // Var94 : CRIT1503, EU00, EU93, EU96
        domainList.add(Domain.builder()
                .name("Var94")
                .values(List.of("CRIT1503", "EU00", "EU93", "EU96"))
                .build());
        // 93
        // Var95 : CRIT1149CC, CRIT1390CC, CRIT1598CC, CRIT1870CC, CRIT1998CC
        domainList.add(Domain.builder()
                .name("Var95")
                .values(List.of("CRIT1149CC", "CRIT1390CC", "CRIT1598CC", "CRIT1870CC", "CRIT1998CC"))
                .build());
        // 94
        // Var96 : CRIT060CV, CRIT065CV, CRIT070CV, CRIT075CV, CRIT090CV, CRIT095CV, CRIT100CV, CRIT105CV, CRIT115CV, CRIT150CV
        domainList.add(Domain.builder()
                .name("Var96")
                .values(List.of("CRIT060CV", "CRIT065CV", "CRIT070CV", "CRIT075CV", "CRIT090CV", "CRIT095CV", "CRIT100CV", "CRIT105CV", "CRIT115CV", "CRIT150CV"))
                .build());
        // 95
        // Var97 : BCNTC, BCTC
        domainList.add(Domain.builder()
                .name("Var97")
                .values(List.of("BCNTC", "BCTC"))
                .build());
        // 96
        // Var98 : AZE, Autre913, CPTECO
        domainList.add(Domain.builder()
                .name("Var98")
                .values(List.of("AZE", "Autre913", "CPTECO"))
                .build());
        // 97
        // Var99 : AD4, DP0, JB1, JB3, JC5
        domainList.add(Domain.builder()
                .name("Var99")
                .values(List.of("AD4", "DP0", "JB1", "JB3", "JC5"))
                .build());
        // 98
        // Var100 : CRIT620, CRIT622, CRIT624, CRIT700, CRIT701, CRIT702, CRIT703, CRIT710, CRIT714, CRIT720, CRIT730, CRIT731, CRIT732, CRIT733, CRIT734, CRIT736, CRIT740, CRIT750, CRIT751, CRIT752, CRIT764, CRIT784, CRIT786, CRIT788, CRIT790, CRIT791, CRIT796, CRIT797, CRIT798
        domainList.add(Domain.builder()
                .name("Var100")
                .values(List.of("CRIT620", "CRIT622", "CRIT624", "CRIT700", "CRIT701", "CRIT702", "CRIT703", "CRIT710", "CRIT714", "CRIT720", "CRIT730", "CRIT731", "CRIT732", "CRIT733", "CRIT734", "CRIT736", "CRIT740", "CRIT750", "CRIT751", "CRIT752", "CRIT764", "CRIT784", "CRIT786", "CRIT788", "CRIT790", "CRIT791", "CRIT796", "CRIT797", "CRIT798"))
                .build());
        // 99
        // Var101 : D7F, E7J, F3R, F4R, F7R, F8Q, F9Q, K4J, K4M, K7M
        domainList.add(Domain.builder()
                .name("Var101")
                .values(List.of("D7F", "E7J", "F3R", "F4R", "F7R", "F8Q", "F9Q", "K4J", "K4M", "K7M"))
                .build());

        LoggerUtils.outdent();
        log.trace("{}<<< Created domains", LoggerUtils.tab());
    }

    public void defineVariables (){
        log.trace("{}Creating variables >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        List<String> varNames = List.of("Var1", "Var2", "Var3", "Var4", "Var5", "Var6", "Var7", "Var8", "Var9", "Var10",
                "Var11", "Var12", "Var13", "Var14", "Var15", "Var16", "Var17", "Var18", "Var19", "Var20",
                "Var21", "Var22", "Var23", "Var24", "Var25", "Var26", "Var27", "Var28", "Var29", "Var30",
                "Var31", "Var32", "Var33", "Var34", "Var35", "Var36", "Var39", "Var40", "Var41", "Var42",
                "Var43", "Var44", "Var45", "Var46", "Var47", "Var48", "Var49", "Var50", "Var51", "Var52",
                "Var53", "Var54", "Var55", "Var56", "Var57", "Var58", "Var59", "Var60", "Var61", "Var62",
                "Var63", "Var64", "Var65", "Var66", "Var67", "Var68", "Var69", "Var70", "Var71", "Var72",
                "Var73", "Var74", "Var75", "Var76", "Var77", "Var78", "Var79", "Var80", "Var81", "Var82",
                "Var83", "Var84", "Var85", "Var86", "Var87", "Var88", "Var89", "Var90", "Var91", "Var92",
                "Var93", "Var94", "Var95", "Var96", "Var97", "Var98", "Var99", "Var100", "Var101");

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
        log.trace("{}Creating constraints >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        ClassLoader classLoader = getClass().getClassLoader();
        for (String ruleFile : ruleFiles) {
            log.trace("{}Reading rule file {}", LoggerUtils.tab(), ruleFile);
            LoggerUtils.indent();

            try {
                @Cleanup InputStream inputStream = getInputStream(classLoader, "renault_rules/renault_rule" + ruleFile);
                @Cleanup BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                int startIdx = modelKB.getNbCstrs();
                org.chocosolver.solver.constraints.Constraint chocoConstraint = readRule(reader);

                Constraint constraint = IntVarConstraintBuilder.build(ruleFile, Collections.emptyList(), modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
//                modelKB.post(chocoConstraint);
//
//                org.chocosolver.solver.constraints.Constraint negChocoConstraint = null;
//                if (hasNegativeConstraints) {
//                    negChocoConstraint = chocoConstraint.getOpposite();
//                    this.modelKB.post(negChocoConstraint);
//                }
//
//                Constraint constraint = new Constraint(ruleFile);
//                ConstraintUtils.addChocoConstraints(constraint, modelKB, startIdx, modelKB.getNbCstrs() - 1, hasNegativeConstraints);
////                constraint.addChocoConstraints(modelKB, startIdx, modelKB.getNbCstrs() - 1, hasNegativeConstraints);
//                constraintList.add(constraint);
//
//                if (hasNegativeConstraints && negChocoConstraint != null) {
//                    modelKB.unpost(negChocoConstraint);
//                }
                constraintList.add(constraint);

            } catch (IOException e) {
                log.error("{}Error while reading rule file {} - {}", LoggerUtils.tab(), ruleFile, e.getMessage());
            }

            LoggerUtils.outdent();
        }

        LoggerUtils.outdent();
        log.trace("{}<<< Created constraints", LoggerUtils.tab());
    }

    @SneakyThrows
    private org.chocosolver.solver.constraints.Constraint readRule(BufferedReader reader) {
        reader.readLine(); // ignore the first line

        return or(reader);
    }

    @SneakyThrows
    private org.chocosolver.solver.constraints.Constraint or(BufferedReader reader) {
        List<org.chocosolver.solver.constraints.Constraint> constraints = new LinkedList<>();

        String line = reader.readLine();


        while (!line.equals("or)")) {
            log.trace("{}Reading rule {}", LoggerUtils.tab(), line);
            if (line.equals("or(")) {
                constraints.add(or(reader));
            } else {
                constraints.add(aLine(line));
            }

            line = reader.readLine();
        }

        org.chocosolver.solver.constraints.Constraint[] arrConstraints = constraints.toArray(new org.chocosolver.solver.constraints.Constraint[0]);
        return modelKB.or(arrConstraints);
    }

    @SneakyThrows
    private org.chocosolver.solver.constraints.Constraint aLine(String line) {
        String[] tokens = line.split(" and ");

        List<org.chocosolver.solver.constraints.Constraint> constraints = new LinkedList<>();
        Arrays.stream(tokens).map(token -> token.split(" = ")).forEachOrdered(var_val -> {
            String variable = var_val[0].trim();
            IntVar intvar = getIntVar(variable);
            String value = var_val[1].trim();
            int chocovalue = getIntValue(variable, value);
            constraints.add(modelKB.arithm(intvar, "=", chocovalue));
        });

        org.chocosolver.solver.constraints.Constraint[] arrConstraints = constraints.toArray(new org.chocosolver.solver.constraints.Constraint[0]);
        return modelKB.and(arrConstraints);
    }

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

//    private int getIndexVariable(String var) {
//        return switch (var) {
//            case "Var1" -> 0;
//            case "Var2" -> 1;
//            case "Var3" -> 2;
//            case "Var4" -> 3;
//            case "Var5" -> 4;
//            case "Var6" -> 5;
//            case "Var7" -> 6;
//            case "Var8" -> 7;
//            case "Var9" -> 8;
//            case "Var10" -> 9;
//            case "Var11" -> 10;
//            case "Var12" -> 11;
//            case "Var13" -> 12;
//            case "Var14" -> 13;
//            case "Var15" -> 14;
//            case "Var16" -> 15;
//            case "Var17" -> 16;
//            case "Var18" -> 17;
//            case "Var19" -> 18;
//            case "Var20" -> 19;
//            case "Var21" -> 20;
//            case "Var22" -> 21;
//            case "Var23" -> 22;
//            case "Var24" -> 23;
//            case "Var25" -> 24;
//            case "Var26" -> 25;
//            case "Var27" -> 26;
//            case "Var28" -> 27;
//            case "Var29" -> 28;
//            case "Var30" -> 29;
//            case "Var31" -> 30;
//            case "Var32" -> 31;
//            case "Var33" -> 32;
//            case "Var34" -> 33;
//            case "Var35" -> 34;
//            case "Var36" -> 35;
//            case "Var39" -> 36;
//            case "Var40" -> 37;
//            case "Var41" -> 38;
//            case "Var42" -> 39;
//            case "Var43" -> 40;
//            case "Var44" -> 41;
//            case "Var45" -> 42;
//            case "Var46" -> 43;
//            case "Var47" -> 44;
//            case "Var48" -> 45;
//            case "Var49" -> 46;
//            case "Var50" -> 47;
//            case "Var51" -> 48;
//            case "Var52" -> 49;
//            case "Var53" -> 50;
//            case "Var54" -> 51;
//            case "Var55" -> 52;
//            case "Var56" -> 53;
//            case "Var57" -> 54;
//            case "Var58" -> 55;
//            case "Var59" -> 56;
//            case "Var60" -> 57;
//            case "Var61" -> 58;
//            case "Var62" -> 59;
//            case "Var63" -> 60;
//            case "Var64" -> 61;
//            case "Var65" -> 62;
//            case "Var66" -> 63;
//            case "Var67" -> 64;
//            case "Var68" -> 65;
//            case "Var69" -> 66;
//            case "Var70" -> 67;
//            case "Var71" -> 68;
//            case "Var72" -> 69;
//            case "Var73" -> 70;
//            case "Var74" -> 71;
//            case "Var75" -> 72;
//            case "Var76" -> 73;
//            case "Var77" -> 74;
//            case "Var78" -> 75;
//            case "Var79" -> 76;
//            case "Var80" -> 77;
//            case "Var81" -> 78;
//            case "Var82" -> 79;
//            case "Var83" -> 80;
//            case "Var84" -> 81;
//            case "Var85" -> 82;
//            case "Var86" -> 83;
//            case "Var87" -> 84;
//            case "Var88" -> 85;
//            case "Var89" -> 86;
//            case "Var90" -> 87;
//            case "Var91" -> 88;
//            case "Var92" -> 89;
//            case "Var93" -> 90;
//            case "Var94" -> 91;
//            case "Var95" -> 92;
//            case "Var96" -> 93;
//            case "Var97" -> 94;
//            case "Var98" -> 95;
//            case "Var99" -> 96;
//            case "Var100" -> 97;
//            case "Var101" -> 98;
//            default -> -1;
//        };
//    }
//
//    private int getIndexValue(String var, String value) {
//        switch (var) {
//            case "Var1":
//                return switch (value) {
//                    case "B64" -> 0;
//                    case "D64" -> 1;
//                    case "E64" -> 2;
//                    case "F64" -> 3;
//                    case "J64" -> 4;
//                    case "K25" -> 5;
//                    case "L64" -> 6;
//                    case "S64" -> 7;
//                    case "V25" -> 8;
//                    default -> -1;
//                };
//            case "Var2":
//                return switch (value) {
//                    case "E0" -> 0;
//                    case "E1" -> 1;
//                    case "E2" -> 2;
//                    case "E3" -> 3;
//                    case "E5" -> 4;
//                    default -> -1;
//                };
//            case "Var3":
//                return switch (value) {
//                    case "M5" -> 0;
//                    case "M6" -> 1;
//                    case "M7" -> 2;
//                    case "M8" -> 3;
//                    case "M9" -> 4;
//                    case "MA" -> 5;
//                    case "MB" -> 6;
//                    case "MC" -> 7;
//                    case "MD" -> 8;
//                    case "ME" -> 9;
//                    case "MF" -> 10;
//                    case "MG" -> 11;
//                    case "MH" -> 12;
//                    case "MJ" -> 13;
//                    case "MK" -> 14;
//                    case "ML" -> 15;
//                    case "MM" -> 16;
//                    case "MN" -> 17;
//                    case "MS" -> 18;
//                    case "MT" -> 19;
//                    case "MU" -> 20;
//                    case "MY" -> 21;
//                    case "ND1G" -> 22;
//                    case "NM0C" -> 23;
//                    case "NM2K" -> 24;
//                    default -> -1;
//                };
//            case "Var4":
//                return switch (value) {
//                    case "DIESEL" -> 0;
//                    case "ESS" -> 1;
//                    default -> -1;
//                };
//            case "Var5":
//                return switch (value) {
//                    case "AFSU" -> 0;
//                    case "ALLE" -> 1;
//                    case "ARGE" -> 2;
//                    case "AUST" -> 3;
//                    case "AUTR" -> 4;
//                    case "BELG" -> 5;
//                    case "BRES" -> 6;
//                    case "CETI" -> 7;
//                    case "CHIL" -> 8;
//                    case "COLO" -> 9;
//                    case "DAIB" -> 10;
//                    case "DAIC" -> 11;
//                    case "DAID" -> 12;
//                    case "DAIF" -> 13;
//                    case "DANE" -> 14;
//                    case "DOTO" -> 15;
//                    case "ESPA" -> 16;
//                    case "EUOR" -> 17;
//                    case "FINL" -> 18;
//                    case "FRAN" -> 19;
//                    case "GRBR" -> 20;
//                    case "GREC" -> 21;
//                    case "HOLL" -> 22;
//                    case "HONG" -> 23;
//                    case "IRLA" -> 24;
//                    case "ISLA" -> 25;
//                    case "ISRA" -> 26;
//                    case "ITAL" -> 27;
//                    case "JAPO" -> 28;
//                    case "MAGH" -> 29;
//                    case "MARO" -> 30;
//                    case "NORV" -> 31;
//                    case "POLO" -> 32;
//                    case "PORT" -> 33;
//                    case "SLVQ" -> 34;
//                    case "SUED" -> 35;
//                    case "SUIS" -> 36;
//                    case "TAIW" -> 37;
//                    case "TCHE" -> 38;
//                    case "TURQ" -> 39;
//                    case "URUG" -> 40;
//                    case "YOUG" -> 41;
//                    default -> -1;
//                };
//            case "Var6":
//                return switch (value) {
//                    case "DD" -> 0;
//                    case "DG" -> 1;
//                    default -> -1;
//                };
//            case "Var7":
//                return switch (value) {
//                    case "GDFROI" -> 0;
//                    case "TEMP" -> 1;
//                    default -> -1;
//                };
//            case "Var8":
//                return switch (value) {
//                    case "CPLG" -> 0;
//                    case "CPLN" -> 1;
//                    default -> -1;
//                };
//            case "Var9":
//                return switch (value) {
//                    case "DA" -> 0;
//                    case "DM" -> 1;
//                    default -> -1;
//                };
//            case "Var10":
//                if ("SANCOA".equals(value)) {
//                    return 0;
//                }
//                return -1;
//            case "Var11":
//                return switch (value) {
//                    case "ABS" -> 0;
//                    case "SSABS" -> 1;
//                    default -> -1;
//                };
//            case "Var12":
//                return switch (value) {
//                    case "GALERI" -> 0;
//                    case "SGALER" -> 1;
//                    default -> -1;
//                };
//            case "Var13":
//                return switch (value) {
//                    case "CA" -> 0;
//                    case "CHAUFO" -> 1;
//                    default -> -1;
//                };
//            case "Var14":
//                return switch (value) {
//                    case "CAPVMA" -> 0;
//                    case "CATOEL" -> 1;
//                    case "CATOMA" -> 2;
//                    case "TN" -> 3;
//                    case "TO" -> 4;
//                    case "TODEGO" -> 5;
//                    default -> -1;
//                };
//            case "Var15":
//                return switch (value) {
//                    case "PBCH" -> 0;
//                    case "PBNCH" -> 1;
//                    default -> -1;
//                };
//            case "Var16":
//                return switch (value) {
//                    case "VC" -> 0;
//                    case "VT" -> 1;
//                    default -> -1;
//                };
//            case "Var17":
//                if ("SPRTEL".equals(value)) {
//                    return 0;
//                }
//                return -1;
//            case "Var18":
//                return switch (value) {
//                    case "ELA" -> 0;
//                    case "SSELA" -> 1;
//                    default -> -1;
//                };
//            case "Var19":
//                return switch (value) {
//                    case "CPE" -> 0;
//                    case "SSCPE" -> 1;
//                    default -> -1;
//                };
//            case "Var20":
//                return switch (value) {
//                    case "SSTIR" -> 0;
//                    case "TIR" -> 1;
//                    default -> -1;
//                };
//            case "Var21":
//                return switch (value) {
//                    case "RETROE" -> 0;
//                    case "RETROR" -> 1;
//                    default -> -1;
//                };
//            case "Var22":
//                return switch (value) {
//                    case "REGSIT" -> 0;
//                    case "SSRSIT" -> 1;
//                    default -> -1;
//                };
//            case "Var23":
//                return switch (value) {
//                    case "PROJAB" -> 0;
//                    case "SPROJA" -> 1;
//                    default -> -1;
//                };
//            case "Var24":
//                if ("SSARCE".equals(value)) {
//                    return 0;
//                }
//                return -1;
//            case "Var25":
//                return switch (value) {
//                    case "CUSFIX" -> 0;
//                    case "CUSPIV" -> 1;
//                    case "SANCUS" -> 2;
//                    default -> -1;
//                };
//            case "Var26":
//                return switch (value) {
//                    case "CLB" -> 0;
//                    case "CLCGRI" -> 1;
//                    case "SANCL" -> 2;
//                    default -> -1;
//                };
//            case "Var27":
//                return switch (value) {
//                    case "ADAC" -> 0;
//                    case "SADAC" -> 1;
//                    default -> -1;
//                };
//            case "Var28":
//                return switch (value) {
//                    case "CCHBAG" -> 0;
//                    case "SCCHBA" -> 1;
//                    default -> -1;
//                };
//            case "Var29":
//                return switch (value) {
//                    case "CUI" -> 0;
//                    case "DRA" -> 1;
//                    default -> -1;
//                };
//            case "Var30":
//                return switch (value) {
//                    case "ATARPH" -> 0;
//                    case "CRIT3ATRPH" -> 1;
//                    case "SSATAR" -> 2;
//                    default -> -1;
//                };
//            case "Var31":
//                return switch (value) {
//                    case "BANAR" -> 0;
//                    case "CRIT3SJAR" -> 1;
//                    case "CRIT3SJARI" -> 2;
//                    case "FBANAR" -> 3;
//                    case "FBARAC" -> 4;
//                    case "SBANAR" -> 5;
//                    default -> -1;
//                };
//            case "Var32":
//                return switch (value) {
//                    case "SGMANU" -> 0;
//                    case "SGMEMO" -> 1;
//                    default -> -1;
//                };
//            case "Var33":
//                return switch (value) {
//                    case "KM" -> 0;
//                    case "MILES" -> 1;
//                    default -> -1;
//                };
//            case "Var34":
//                return switch (value) {
//                    case "Autre167" -> 0;
//                    case "CPK01" -> 1;
//                    case "CPK02" -> 2;
//                    default -> -1;
//                };
//            case "Var35":
//                return switch (value) {
//                    case "RIDOAR" -> 0;
//                    case "SRIDAR" -> 1;
//                    default -> -1;
//                };
//            case "Var36":
//                if ("PTCAV".equals(value)) {
//                    return 0;
//                }
//                return -1;
//            case "Var39":
//                return switch (value) {
//                    case "CATADI" -> 0;
//                    case "SCATAD" -> 1;
//                    default -> -1;
//                };
//            case "Var40":
//                return switch (value) {
//                    case "EMBNOR" -> 0;
//                    case "EMBPIL" -> 1;
//                    case "SEMBRY" -> 2;
//                    default -> -1;
//                };
//            case "Var41":
//                return switch (value) {
//                    case "PNERFL" -> 0;
//                    case "PNESTD" -> 1;
//                    default -> -1;
//                };
//            case "Var42":
//                return switch (value) {
//                    case "PHAN01" -> 0;
//                    case "PHAN02" -> 1;
//                    case "SSPHAN" -> 2;
//                    default -> -1;
//                };
//            case "Var43":
//                return switch (value) {
//                    case "ETPN01" -> 0;
//                    case "ETPN02" -> 1;
//                    case "SSETPN" -> 2;
//                    default -> -1;
//                };
//            case "Var44":
//                return switch (value) {
//                    case "Autre272" -> 0;
//                    case "EQGPL" -> 1;
//                    case "PREGPL" -> 2;
//                    default -> -1;
//                };
//            case "Var45":
//                return switch (value) {
//                    case "SUSNOR" -> 0;
//                    case "SUSREN" -> 1;
//                    default -> -1;
//                };
//            case "Var46":
//                return switch (value) {
//                    case "RENTC" -> 0;
//                    case "RETC" -> 1;
//                    default -> -1;
//                };
//            case "Var47":
//                return switch (value) {
//                    case "LVAVEL" -> 0;
//                    case "LVAVMA" -> 1;
//                    default -> -1;
//                };
//            case "Var48":
//                return switch (value) {
//                    case "LVAREL" -> 0;
//                    case "LVARMA" -> 1;
//                    case "SSLVAR" -> 2;
//                    default -> -1;
//                };
//            case "Var49":
//                return switch (value) {
//                    case "Autre310" -> 0;
//                    case "SSAMVA" -> 1;
//                    default -> -1;
//                };
//            case "Var50":
//                if ("SASURV".equals(value)) {
//                    return 0;
//                }
//                return -1;
//            case "Var51":
//                return switch (value) {
//                    case "SGACHA" -> 0;
//                    case "SGSCHA" -> 1;
//                    default -> -1;
//                };
//            case "Var52":
//                return switch (value) {
//                    case "COFIXE" -> 0;
//                    case "COLOMB" -> 1;
//                    case "COREHA" -> 2;
//                    case "CORHLO" -> 3;
//                    default -> -1;
//                };
//            case "Var53":
//                return switch (value) {
//                    case "AVPFIL" -> 0;
//                    case "SSPFIL" -> 1;
//                    default -> -1;
//                };
//            case "Var54":
//                return switch (value) {
//                    case "NAVIG" -> 0;
//                    case "SNAVIG" -> 1;
//                    default -> -1;
//                };
//            case "Var55":
//                return switch (value) {
//                    case "CRIT2RHENF" -> 0;
//                    case "RHENF" -> 1;
//                    case "SRHENF" -> 2;
//                    default -> -1;
//                };
//            case "Var56":
//                return switch (value) {
//                    case "APL01" -> 0;
//                    case "APL02" -> 1;
//                    default -> -1;
//                };
//            case "Var57":
//                return switch (value) {
//                    case "AILAR" -> 0;
//                    case "SAILAR" -> 1;
//                    default -> -1;
//                };
//            case "Var58":
//                return switch (value) {
//                    case "MGMAZE" -> 0;
//                    case "MGMECO" -> 1;
//                    case "MGMINI" -> 2;
//                    case "MGMRNA" -> 3;
//                    case "MGMRNE" -> 4;
//                    case "MGMRTA" -> 5;
//                    case "MGMRTE" -> 6;
//                    case "MGMRXT" -> 7;
//                    case "RL" -> 8;
//                    case "RN" -> 9;
//                    case "RT" -> 10;
//                    case "RXE" -> 11;
//                    case "SMONEQ" -> 12;
//                    default -> -1;
//                };
//            case "Var59":
//                return switch (value) {
//                    case "RUNLI" -> 0;
//                    case "SRUNLI" -> 1;
//                    default -> -1;
//                };
//            case "Var60":
//                return switch (value) {
//                    case "LAVPH" -> 0;
//                    case "SLAVPH" -> 1;
//                    default -> -1;
//                };
//            case "Var61":
//                return switch (value) {
//                    case "EVCFIX" -> 0;
//                    case "EVCVAR" -> 1;
//                    default -> -1;
//                };
//            case "Var62":
//                return switch (value) {
//                    case "AOEF" -> 0;
//                    case "SAOEF" -> 1;
//                    default -> -1;
//                };
//            case "Var63":
//                return switch (value) {
//                    case "VOLNRH" -> 0;
//                    case "VOLRH" -> 1;
//                    default -> -1;
//                };
//            case "Var64":
//                return switch (value) {
//                    case "PANP00" -> 0;
//                    case "PANP01" -> 1;
//                    case "PANP02" -> 2;
//                    case "PANP03" -> 3;
//                    case "PANP05" -> 4;
//                    default -> -1;
//                };
//            case "Var65":
//                return switch (value) {
//                    case "PLAFNT" -> 0;
//                    case "PLAFT" -> 1;
//                    default -> -1;
//                };
//            case "Var66":
//                return switch (value) {
//                    case "JANALU" -> 0;
//                    case "JANTOL" -> 1;
//                    default -> -1;
//                };
//            case "Var67":
//                return switch (value) {
//                    case "T0" -> 0;
//                    case "T1" -> 1;
//                    case "T2" -> 2;
//                    case "T3" -> 3;
//                    case "T5" -> 4;
//                    default -> -1;
//                };
//            case "Var68":
//                return switch (value) {
//                    case "PSCOMI" -> 0;
//                    case "PSCOMR" -> 1;
//                    case "PSCOPO" -> 2;
//                    default -> -1;
//                };
//            case "Var69":
//                return switch (value) {
//                    case "PSPAMI" -> 0;
//                    case "PSPAMR" -> 1;
//                    default -> -1;
//                };
//            case "Var70":
//                return switch (value) {
//                    case "ETAP01" -> 0;
//                    case "ETAP03" -> 1;
//                    case "SSETAP" -> 2;
//                    default -> -1;
//                };
//            case "Var71":
//                if ("FSTPO".equals(value)) {
//                    return 0;
//                }
//                return -1;
//            case "Var72":
//                return switch (value) {
//                    case "DUCA" -> 0;
//                    case "FUJI" -> 1;
//                    case "KANG" -> 2;
//                    case "ODIN" -> 3;
//                    case "PARALL" -> 4;
//                    case "PARBRE" -> 5;
//                    case "PARGBR" -> 6;
//                    case "PARPOL" -> 7;
//                    case "PARTCH" -> 8;
//                    case "SSEDNC" -> 9;
//                    case "VRMI" -> 10;
//                    default -> -1;
//                };
//            case "Var73":
//                return switch (value) {
//                    case "TBOR00" -> 0;
//                    case "TBOR01" -> 1;
//                    case "TBOR02" -> 2;
//                    case "TBOR03" -> 3;
//                    case "TBOR05" -> 4;
//                    default -> -1;
//                };
//            case "Var74":
//                return switch (value) {
//                    case "PBOR00" -> 0;
//                    case "PBOR01" -> 1;
//                    case "PBOR02" -> 2;
//                    case "PBOR03" -> 3;
//                    case "PBOR05" -> 4;
//                    default -> -1;
//                };
//            case "Var75":
//                return switch (value) {
//                    case "MOCY01" -> 0;
//                    case "MOCY02" -> 1;
//                    case "MOCY03" -> 2;
//                    case "MOCY04" -> 3;
//                    case "MOCY05" -> 4;
//                    case "MOCY06" -> 5;
//                    case "MOCY07" -> 6;
//                    case "MOCY08" -> 7;
//                    case "MOCY10" -> 8;
//                    case "MOCY11" -> 9;
//                    case "MOCY12" -> 10;
//                    case "SSMOCY" -> 11;
//                    default -> -1;
//                };
//            case "Var76":
//                return switch (value) {
//                    case "Autre408" -> 0;
//                    case "NINAV1" -> 1;
//                    default -> -1;
//                };
//            case "Var77":
//                return switch (value) {
//                    case "ABPA01" -> 0;
//                    case "SSABPA" -> 1;
//                    default -> -1;
//                };
//            case "Var78":
//                return switch (value) {
//                    case "FIPOU" -> 0;
//                    case "SFIPOU" -> 1;
//                    default -> -1;
//                };
//            case "Var79":
//                return switch (value) {
//                    case "ABCO01" -> 0;
//                    case "SSABCO" -> 1;
//                    default -> -1;
//                };
//            case "Var80":
//                return switch (value) {
//                    case "ANSRAD" -> 0;
//                    case "CRIT2X5KS" -> 1;
//                    case "CRIT2X8KI" -> 2;
//                    case "CRIT4X15CI" -> 3;
//                    case "CRIT4X15KI" -> 4;
//                    case "CRIT4X25KI" -> 5;
//                    case "SRADIO" -> 6;
//                    default -> -1;
//                };
//            case "Var81":
//                return switch (value) {
//                    case "BVA4" -> 0;
//                    case "BVM5" -> 1;
//                    default -> -1;
//                };
//            case "Var82":
//                return switch (value) {
//                    case "NMAS01" -> 0;
//                    case "NMAS02" -> 1;
//                    case "NMAS03" -> 2;
//                    case "NMAS04" -> 3;
//                    case "NMAS05" -> 4;
//                    default -> -1;
//                };
//            case "Var83":
//                return switch (value) {
//                    case "Autre432" -> 0;
//                    case "PLARPT" -> 1;
//                    case "PLARVN" -> 2;
//                    case "PLARVO" -> 3;
//                    default -> -1;
//                };
//            case "Var84":
//                return switch (value) {
//                    case "ECLAR" -> 0;
//                    case "SECLAR" -> 1;
//                    default -> -1;
//                };
//            case "Var85":
//                return switch (value) {
//                    case "CDCOF" -> 0;
//                    case "SCDCOF" -> 1;
//                    default -> -1;
//                };
//            case "Var86":
//                return switch (value) {
//                    case "ACPLAR" -> 0;
//                    case "SACPLA" -> 1;
//                    default -> -1;
//                };
//            case "Var87":
//                return switch (value) {
//                    case "MONORM" -> 0;
//                    case "SURMO1" -> 1;
//                    default -> -1;
//                };
//            case "Var88":
//                return switch (value) {
//                    case "Autre497" -> 0;
//                    case "JANDIF" -> 1;
//                    default -> -1;
//                };
//            case "Var89":
//                return switch (value) {
//                    case "Autre513" -> 0;
//                    case "EVA" -> 1;
//                    case "EVE" -> 2;
//                    default -> -1;
//                };
//            case "Var90":
//                return switch (value) {
//                    case "ANTID" -> 0;
//                    case "ANTIDI" -> 1;
//                    case "SDPCLV" -> 2;
//                    default -> -1;
//                };
//            case "Var91":
//                return switch (value) {
//                    case "Autre613" -> 0;
//                    case "TKO" -> 1;
//                    default -> -1;
//                };
//            case "Var92":
//                return switch (value) {
//                    case "Autre713" -> 0;
//                    case "CPNT01" -> 1;
//                    case "CPNT02" -> 2;
//                    case "CPNT03" -> 3;
//                    case "EQDIF" -> 4;
//                    default -> -1;
//                };
//            case "Var93":
//                return switch (value) {
//                    case "Autre714" -> 0;
//                    case "BVDIF" -> 1;
//                    default -> -1;
//                };
//            case "Var94":
//                return switch (value) {
//                    case "CRIT1503" -> 0;
//                    case "EU00" -> 1;
//                    case "EU93" -> 2;
//                    case "EU96" -> 3;
//                    default -> -1;
//                };
//            case "Var95":
//                return switch (value) {
//                    case "CRIT1149CC" -> 0;
//                    case "CRIT1390CC" -> 1;
//                    case "CRIT1598CC" -> 2;
//                    case "CRIT1870CC" -> 3;
//                    case "CRIT1998CC" -> 4;
//                    default -> -1;
//                };
//            case "Var96":
//                return switch (value) {
//                    case "CRIT060CV" -> 0;
//                    case "CRIT065CV" -> 1;
//                    case "CRIT070CV" -> 2;
//                    case "CRIT075CV" -> 3;
//                    case "CRIT090CV" -> 4;
//                    case "CRIT095CV" -> 5;
//                    case "CRIT100CV" -> 6;
//                    case "CRIT105CV" -> 7;
//                    case "CRIT115CV" -> 8;
//                    case "CRIT150CV" -> 9;
//                    default -> -1;
//                };
//            case "Var97":
//                return switch (value) {
//                    case "BCNTC" -> 0;
//                    case "BCTC" -> 1;
//                    default -> -1;
//                };
//            case "Var98":
//                return switch (value) {
//                    case "AZE" -> 0;
//                    case "Autre913" -> 1;
//                    case "CPTECO" -> 2;
//                    default -> -1;
//                };
//            case "Var99":
//                return switch (value) {
//                    case "AD4" -> 0;
//                    case "DP0" -> 1;
//                    case "JB1" -> 2;
//                    case "JB3" -> 3;
//                    case "JC5" -> 4;
//                    default -> -1;
//                };
//            case "Var100":
//                return switch (value) {
//                    case "CRIT620" -> 0;
//                    case "CRIT622" -> 1;
//                    case "CRIT624" -> 2;
//                    case "CRIT700" -> 3;
//                    case "CRIT701" -> 4;
//                    case "CRIT702" -> 5;
//                    case "CRIT703" -> 6;
//                    case "CRIT710" -> 7;
//                    case "CRIT714" -> 8;
//                    case "CRIT720" -> 9;
//                    case "CRIT730" -> 10;
//                    case "CRIT731" -> 11;
//                    case "CRIT732" -> 12;
//                    case "CRIT733" -> 13;
//                    case "CRIT734" -> 14;
//                    case "CRIT736" -> 15;
//                    case "CRIT740" -> 16;
//                    case "CRIT750" -> 17;
//                    case "CRIT751" -> 18;
//                    case "CRIT752" -> 19;
//                    case "CRIT764" -> 20;
//                    case "CRIT784" -> 21;
//                    case "CRIT786" -> 22;
//                    case "CRIT788" -> 23;
//                    case "CRIT790" -> 24;
//                    case "CRIT791" -> 25;
//                    case "CRIT796" -> 26;
//                    case "CRIT797" -> 27;
//                    case "CRIT798" -> 28;
//                    default -> -1;
//                };
//            case "Var101":
//                return switch (value) {
//                    case "D7F" -> 0;
//                    case "E7J" -> 1;
//                    case "F3R" -> 2;
//                    case "F4R" -> 3;
//                    case "F7R" -> 4;
//                    case "F8Q" -> 5;
//                    case "F9Q" -> 6;
//                    case "K4J" -> 7;
//                    case "K4M" -> 8;
//                    case "K7M" -> 9;
//                    default -> -1;
//                };
//            default: return -1;
//        }
//    }
}
