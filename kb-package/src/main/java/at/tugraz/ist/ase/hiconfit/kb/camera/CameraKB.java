/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.camera;

import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.*;
import at.tugraz.ist.ase.hiconfit.kb.core.builder.IntVarConstraintBuilder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class CameraKB extends KB implements IIntVarKB {
    public CameraKB(boolean hasNegativeConstraints) {
        super("Camera Configuration Problem", "https://github.com/CSPHeuristix/CDBC/blob/master/CameraKB.java", hasNegativeConstraints);

        reset(hasNegativeConstraints);
    }

    @Override
    public void reset(boolean hasNegativeConstraints) {
        log.trace("{}Creating CameraKB >>>", LoggerUtils.tab());
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
        log.debug("{}<<< Created CameraKB", LoggerUtils.tab());
    }

    private void defineDomains() {
        log.trace("{}Defining domains >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        domainList.add(Domain.builder() // 11 items
                .name("Resolution")
                .values(List.of("61", "102", "123", "142", "162", "241", "242", "208", "209", "243", "363"))
                .chocoValues(List.of(61, 102, 123, 142, 162, 241, 242, 208, 209, 243, 363))
                .build());
        domainList.add(Domain.builder() // 5 items
                .name("Display")
                .values(List.of("18", "25", "27", "30", "32"))
                .chocoValues(List.of(18, 25, 27, 30, 32))
                .build());
        domainList.add(Domain.builder() // binary
                .name("Touchscreen")
                .values(List.of("0", "1"))
                .build());
        domainList.add(Domain.builder() // binary
                .name("WiFi")
                .values(List.of("0", "1"))
                .build());
        domainList.add(Domain.builder() // binary
                .name("NFC")
                .values(List.of("0", "1"))
                .build());
        domainList.add(Domain.builder() // binary
                .name("GPS")
                .values(List.of("0", "1"))
                .build());
        domainList.add(Domain.builder() // 6 items
                .name("Video")
                .values(List.of("0", "1", "2", "3", "4", "5"))
                .build());
        domainList.add(Domain.builder() // 6 items
                .name("Zoom")
                .values(List.of("20", "30", "35", "50", "58", "78"))
                .chocoValues(List.of(20, 30, 35, 50, 58, 78))
                .build());
        domainList.add(Domain.builder() // 17 items
                .name("Weight")
                .values(List.of("445", "455", "460", "470", "475", "505", "530", "535", "560", "675", "700", "765", "840", "850", "860", "980", "1405"))
                .chocoValues(List.of(445, 455, 460, 470, 475, 505, 530, 535, 560, 675, 700, 765, 840, 850, 860, 980, 1405))
                .build());
        domainList.add(Domain.builder() // 19 items
                .name("Price")
                .values(List.of("189", "399", "400", "469", "479", "499", "579", "581", "609", "659", "669", "749", "1129", "1649", "2149", "2329", "2749", "3229", "5219"))
                .chocoValues(List.of(189, 399, 400, 469, 479, 499, 579, 581, 609, 659, 669, 749, 1129, 1649, 2149, 2329, 2749, 3229, 5219))
                .build());

        LoggerUtils.outdent();
        log.trace("{}<<< Created domains", LoggerUtils.tab());
    }

    public void defineVariables (){
        log.trace("{}Defining variables >>", LoggerUtils.tab());
        LoggerUtils.indent();

        List<String> varNames = List.of("Resolution", "Display", "Touchscreen", "WiFi", "NFC",
                "GPS", "Video", "Zoom", "Weight", "Price");

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
        log.trace("{}Defining constraints >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        int startIdx = 0;

        List<org.chocosolver.solver.constraints.Constraint> kbReqList = new ArrayList<>();
        //C1 - Resolution=208,Display=32,Touchscreen=1,WiFi=1,NFC=0,GPS=1,Video=0,Zoom=30,Weight=1405,Price=5219
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",208));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",32));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",1405));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",5219));

        //C2 - Resolution=61,Display=25,Touchscreen=0,WiFi=1,NFC=0,GPS=0,Video=4,Zoom=30,Weight=475,Price=659
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",61));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",25));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",4));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",475));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",659));

        //C3 - Resolution=61,Display=18,Touchscreen=0,WiFi=0,NFC=0,GPS=0,Video=4,Zoom=20,Weight=700,Price=189
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",61));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",18));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",4));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",20));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",700));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",189));

        //C4 - Resolution=209,Display=32,Touchscreen=1,WiFi=1,NFC=1,GPS=0,Video=0,Zoom=58,Weight=860,Price=2329
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",209));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",32));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",58));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",860));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",2329));

        //C5 - Resolution=243,Display=32,Touchscreen=0,WiFi=0,NFC=0,GPS=0,Video=2,Zoom=35,Weight=850,Price=1649
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",243));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",32));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",2));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",35));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",850));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",1649));

        //C6 - Resolution=243,Display=32,Touchscreen=0,WiFi=1,NFC=0,GPS=0,Video=3,Zoom=35,Weight=840,Price=2149
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",243));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",32));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",3));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",35));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",840));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",2149));

        //C7 - Resolution=363,Display=32,Touchscreen=0,WiFi=0,NFC=0,GPS=0,Video=3,Zoom=50,Weight=980,Price=3229
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",363));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",32));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",3));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",50));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",980));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",3229));

        //C8 - Resolution=102,Display=30,Touchscreen=0,WiFi=0,NFC=0,GPS=0,Video=4,Zoom=30,Weight=535,Price=400
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",102));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",4));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",535));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",400));

        //C9 - Resolution=142,Display=30,Touchscreen=0,WiFi=0,NFC=0,GPS=0,Video=1,Zoom=30,Weight=455,Price=469
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",142));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",455));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",469));

        //C10 - Resolution=242,Display=30,Touchscreen=0,WiFi=0,NFC=0,GPS=1,Video=2,Zoom=30,Weight=455,Price=581
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",242));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",2));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",455));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",581));

        //C11 - Resolution=242,Display=30,Touchscreen=0,WiFi=0,NFC=0,GPS=0,Video=3,Zoom=58,Weight=460,Price=399
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",242));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",3));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",58));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",460));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",399));

        //C12 - Resolution=242,Display=30,Touchscreen=0,WiFi=0,NFC=0,GPS=0,Video=3,Zoom=30,Weight=445,Price=499
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",242));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",3));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",445));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",499));

        //C13 - Resolution=123,Display=27,Touchscreen=0,WiFi=0,NFC=0,GPS=1,Video=5,Zoom=30,Weight=560,Price=579
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",123));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",27));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",5));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",560));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",579));

        //C14 - Resolution=162,Display=30,Touchscreen=0,WiFi=0,NFC=0,GPS=1,Video=2,Zoom=30,Weight=560,Price=469
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",162));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",2));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",560));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",469));

        //C15 - Resolution=241,Display=30,Touchscreen=0,WiFi=1,NFC=0,GPS=1,Video=2,Zoom=58,Weight=505,Price=479
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",241));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",30));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",2));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",58));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",505));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",479));

        //C16 - Resolution=242,Display=32,Touchscreen=0,WiFi=1,NFC=0,GPS=1,Video=3,Zoom=58,Weight=530,Price=609
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",242));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",32));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",3));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",58));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",530));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",609));

        //C17 - Resolution=242,Display=32,Touchscreen=1,WiFi=1,NFC=0,GPS=1,Video=3,Zoom=58,Weight=470,Price=749
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",242));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",32));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",3));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",58));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",470));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",749));

        //C18 - Resolution=241,Display=32,Touchscreen=1,WiFi=1,NFC=0,GPS=1,Video=2,Zoom=58,Weight=675,Price=669
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",241));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",32));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",2));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",58));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",675));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",669));

        //C19 - Resolution=242,Display=32,Touchscreen=0,WiFi=1,NFC=1,GPS=0,Video=3,Zoom=78,Weight=765,Price=1129
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",242));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",32));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",1));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",3));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",78));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",765));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",1129));

        //C20 - Resolution=162,Display=32,Touchscreen=0,WiFi=0,NFC=0,GPS=0,Video=4,Zoom=50,Weight=765,Price=2749
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(0)).getChocoVar(),"=",162));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(1)).getChocoVar(),"=",32));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(2)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(3)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(4)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(5)).getChocoVar(),"=",0));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(6)).getChocoVar(),"=",4));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(7)).getChocoVar(),"=",50));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(8)).getChocoVar(),"=",765));
        kbReqList.add(this.modelKB.arithm( ((IntVariable)variableList.get(9)).getChocoVar(),"=",2749));

//        List<org.chocosolver.solver.constraints.Constraint> configurationList = IntStream.iterate(0, counter -> counter <= kbReqList.size() - 10, counter -> counter + 10)
//                .mapToObj(counter -> this.modelKB.and(kbReqList.get(counter), kbReqList.get(1 + counter), kbReqList.get(2 + counter),
//                        kbReqList.get(3 + counter), kbReqList.get(4 + counter), kbReqList.get(5 + counter), kbReqList.get(6 + counter),
//                        kbReqList.get(7 + counter), kbReqList.get(8 + counter), kbReqList.get(9 + counter))).toList();
        List<org.chocosolver.solver.constraints.Constraint> configurationList = new ArrayList<>();
        for (int counter = 0; counter <= kbReqList.size() - 10; counter = counter + 10) {
            configurationList.add(this.modelKB.and(kbReqList.get(counter), kbReqList.get(1 + counter), kbReqList.get(2 + counter),
                    kbReqList.get(3 + counter), kbReqList.get(4 + counter), kbReqList.get(5 + counter), kbReqList.get(6 + counter), kbReqList.get(7 + counter),
                    kbReqList.get(8 + counter), kbReqList.get(9 + counter)));
        }

        org.chocosolver.solver.constraints.Constraint chocoConstraint = this.modelKB.or(configurationList.get(0), configurationList.get(1), configurationList.get(2), configurationList.get(3), configurationList.get(4),
                configurationList.get(5), configurationList.get(6), configurationList.get(7), configurationList.get(8), configurationList.get(9), configurationList.get(10),
                configurationList.get(11), configurationList.get(12), configurationList.get(13), configurationList.get(14), configurationList.get(15), configurationList.get(16),
                configurationList.get(17), configurationList.get(18), configurationList.get(19));

//        addConstraint("kb", chocoConstraint, startIdx, hasNegativeConstraints);
        Constraint constraint = IntVarConstraintBuilder.build("kb", modelKB, chocoConstraint, startIdx, hasNegativeConstraints);
        constraintList.add(constraint);

        LoggerUtils.outdent();
        log.trace("{}<<< Created constraints", LoggerUtils.tab());
    }

//    private void addConstraint(String constraintName, org.chocosolver.solver.constraints.Constraint chocoConstraint, int startIdx, boolean hasNegativeConstraints) {
//        modelKB.post(chocoConstraint);
//
//        org.chocosolver.solver.constraints.Constraint negChocoConstraint = null;
//        if (hasNegativeConstraints) {
//            negChocoConstraint = chocoConstraint.getOpposite();
//            modelKB.post(negChocoConstraint);
//        }
//
//        Constraint constraint = new Constraint(constraintName);
//        ConstraintUtils.addChocoConstraints(constraint, modelKB, startIdx, modelKB.getNbCstrs() - 1, hasNegativeConstraints);
////        constraint.addChocoConstraints(modelKB, startIdx, modelKB.getNbCstrs() - 1, hasNegativeConstraints);
//        constraintList.add(constraint);
//
//        if (hasNegativeConstraints && negChocoConstraint != null) {
//            modelKB.unpost(negChocoConstraint);
//        }
//    }

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
}
