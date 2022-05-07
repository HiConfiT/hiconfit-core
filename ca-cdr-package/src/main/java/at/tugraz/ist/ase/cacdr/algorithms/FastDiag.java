/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of FastDiag algorithm using List structures.
 *
 * <ul>
 *     <li>Felfernig, Alexander, Monika Schubert, and Christoph Zehentner.
 *     "An efficient diagnosis algorithm for inconsistent constraint sets."
 *     Artificial Intelligence for Engineering Design, Analysis and Manufacturing:
 *     AI EDAM 26.1 (2012): 53.</li>
 * </ul>
 *
 * //FastDiag Algorithm
 * //--------------------
 * //func FastDiag(C ⊆ AC, AC = {c1..ct}) :  Δ
 * //if isEmpty(C) or inconsistent(AC - C) return Φ
 * //else return FD(Φ, C, AC)
 *
 * //func FD(D, C = {c1..cq}, AC) : diagnosis  Δ
 * //if D != Φ and consistent(AC) return Φ;
 * //if singleton(C) return C;
 * //k = q/2;
 * //C1 = {c1..ck}; C2 = {ck+1..cq};
 * //D1 = FD(C1, C2, AC - C1);
 * //D2 = FD(D1, C1, AC - D1);
 * //return(D2 ∪ D1);
 *
 * @author Muslum Atas (muesluem.atas@ist.tugraz.at)
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Deprecated
public class FastDiag {

    public static List<Constraint> fastDiag(List<Constraint> c, List<Constraint> ac, Model model)
    {
//        incrementCounter(COUNTER_FASTDIAG_CALLS);
        List<Constraint> acOriginal = new ArrayList<Constraint>(); acOriginal.addAll(ac); //incrementCounter(COUNTER_ADD_OPERATOR);
        //if isEmpty(C) or inconsistent(AC - C) return Φ
        if (c.isEmpty())
            return Collections.<Constraint>emptyList();
        if (ac.containsAll(c) && isConsistent(ac, model)) {
//            incrementCounter(COUNTER_CONTAINSALL_CHECKS);
            return Collections.<Constraint>emptyList();
        }
        if (!isConsistent(subConstrsSets(acOriginal,c), model)) {
//            incrementCounter(COUNTER_DIFFERENT_OPERATOR);
            return Collections.<Constraint>emptyList();
        } else{ //else return FD(Φ, C, AC)
            List<Constraint> emptyList = new ArrayList<Constraint>();
            return fd(emptyList, c, ac, model);}
    }
    // func FD(D, C = {c1..cq}, AC) : diagnosis  Δ
    private static List<Constraint> fd(List<Constraint> d, List<Constraint> c, List<Constraint> ac, Model model){
        List<Constraint> diagnosis = new ArrayList<Constraint>();
        int cSize=c.size();
        // if D != Φ and consistent(AC) return Φ;
        if( !d.isEmpty() && isConsistent(ac, model))
            return Collections.<Constraint>emptyList();
        // if singleton(C) return C;
        if(cSize==1)
            return c;
        int k = cSize/2;  // k = q/2;
        // C1 = {c1..ck}; C2 = {ck+1..cq};
        List<Constraint> c1 = new ArrayList<Constraint>();
        c1.addAll(c.subList(0, k));
        List<Constraint> c2 = new ArrayList<Constraint>();
        c2.addAll(c.subList(k, cSize));
//        incrementCounter(COUNTER_SPLIT_SET);
        //Saving AC of the parent node
        List<Constraint> prevAC=new ArrayList<Constraint>();
        prevAC.addAll(ac); //incrementCounter(COUNTER_ADD_OPERATOR);
        List<Constraint> acTemp = new ArrayList<Constraint>();
        acTemp.addAll(ac); //incrementCounter(COUNTER_ADD_OPERATOR);

        // D1 = FD(C1, C2, AC - C1);
//        incrementCounter(COUNTER_DIFFERENT_OPERATOR);
//        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        List<Constraint> d1 = fd(c1, c2, subConstrsSets(acTemp,c1),model);

        // D2 = FD(D1, C1, AC - D1);
//        incrementCounter(COUNTER_DIFFERENT_OPERATOR);
//        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        List<Constraint> d2 = fd(d1, c1, subConstrsSets(prevAC,d1),model);

        diagnosis.addAll(d2); //incrementCounter(COUNTER_ADD_OPERATOR);
        for (int i=0; i<d1.size(); i++) {
//            incrementCounter(COUNTER_CONTAINSALL_CHECKS);
            if (!diagnosis.contains(d1.get(i))) {
                diagnosis.add(d1.get(i)); //incrementCounter(COUNTER_ADD_OPERATOR);
            }
        }

        return diagnosis;
    }
    // Check if set of constraint is consistent
    private static boolean isConsistent(List<Constraint> constrs, Model model)
    {
        model.getSolver().reset();
        model.unpost(model.getCstrs());
        for (int i=0; i<constrs.size(); i++)
        {
            model.post(constrs.get(i));
        }
//        incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, model.getNbCstrs());
//        incrementCounter(COUNTER_CONSISTENCY_CHECKS);
        return model.getSolver().solve();
    }

    //Calculate c1-c2
    private static List<Constraint> subConstrsSets(List<Constraint> c1, List<Constraint> c2)
    {
        c1.removeAll(c2);
        return c1;
    }

    //calculate all diagnosis starting from the first diagnosis using FastDiag
    public static List<List<Constraint>> calculateAllDiagnoses(List<Constraint> firstDiag, List<Constraint> c, List<Constraint> ac, Model model, List<List<Constraint>> allDiag)
    {
        allDiag.add(firstDiag); //incrementCounter(COUNTER_ADD_OPERATOR);
        List<List<Constraint>> nodeDiagnoses= new ArrayList<List<Constraint>>();
        List<List<Constraint>> childCnstnts= new ArrayList<List<Constraint>>();

//        incrementCounter(COUNTER_EXPLORE_NODE_CALLS);
        nodeDiagnoses(firstDiag, c, ac, model, nodeDiagnoses, allDiag, childCnstnts);

        while (!nodeDiagnoses.isEmpty())
        {
            List<List<Constraint>> childDiagnoses= new ArrayList<List<Constraint>>();
            List<List<Constraint>> childConstraints= new ArrayList<List<Constraint>>();
            for (int j=0; j<nodeDiagnoses.size(); j++)
            {
//                incrementCounter(COUNTER_EXPLORE_NODE_CALLS);
                nodeDiagnoses(nodeDiagnoses.get(j), childCnstnts.get(j), ac, model, childDiagnoses, allDiag, childConstraints);
            }
            nodeDiagnoses=childDiagnoses;
            childCnstnts=childConstraints;
        }
        return allDiag;
    }

    //Calculate diagnoses from a node depending on FastDiag (returns children (diagnoses) of a node)
    private static List<List<Constraint>> nodeDiagnoses(List<Constraint> node, List<Constraint> c,List<Constraint> ac, Model model, List<List<Constraint>> nodeDiag, List<List<Constraint>> allDiag, List<List<Constraint>> childC)
    {
        for (int i = node.size() - 1; i >= 0; i--)
        {
            List<Constraint> acOriginal=new ArrayList<Constraint>();  acOriginal.addAll(ac); // after calling fastDiag, ac changes so we need to restore its original value.
            List<Constraint> cOriginal=new ArrayList<Constraint>();  cOriginal.addAll(c); // after calling fastDiag, c changes so we need to restore its original value.
//            incrementCounter(COUNTER_ADD_OPERATOR, 2);

            Constraint constr=node.get(i);

            cOriginal.remove(constr); //incrementCounter(COUNTER_DIFFERENT_OPERATOR);
            List<Constraint> diag=new ArrayList<Constraint>();
            diag=fastDiag(cOriginal, acOriginal,model);
            if (!diag.isEmpty() && isMinimal(diag,allDiag) && !allDiag.containsAll(diag))
            {
//                incrementCounter(COUNTER_CONTAINSALL_CHECKS);
//                incrementCounter(COUNTER_ADD_OPERATOR, 3);
                nodeDiag.add(diag);
                allDiag.add(diag);
                childC.add(cOriginal); // saving the constraints set (c) of each child (diagnosis) to be used on the next call.
            }
        }
        return nodeDiag;
    }

    private static boolean isMinimal(List<Constraint> diag, List<List<Constraint>> allDiag)
    {
//        incrementCounter(COUNTER_ISMINIMAL_CALLS);
        boolean minimal=true;
        for (int i=0; i<allDiag.size() && minimal; i++)
        {
//            incrementCounter(COUNTER_CONTAINSALL_CHECKS);
            if (diag.containsAll(allDiag.get(i)))
                minimal=false;
            else
                minimal=true;
        }
        return minimal;
    }

    //Calculate all diagnoses based on resolving the conflict sets of QuickXplain (HSDAG)
//    public static List<List<Constraint>> nodeConflictSets(List<Constraint> node, List<Constraint> b, List<Constraint> c, List<List<Constraint>> nodeConflicts, Model model, List<List<Constraint>> childC, List<Constraint> parentDiag,List<List<Constraint>> parentsDiag, List<List<Constraint>> allDiagnoses)
//    {
//        long startTime = System.currentTimeMillis();
//
//        List<Constraint> conflictSet=new ArrayList<Constraint>();
//        for (int i=0; i<node.size();i++)
//        {
//            Constraint constr=node.get(i);
//            List<Constraint> bOriginal=new ArrayList<Constraint>(); bOriginal.addAll(b);
//            List<Constraint> cOriginal=new ArrayList<Constraint>(); cOriginal.addAll(c);
//            cOriginal.remove(constr);
//            List<Constraint> constrList= new ArrayList<Constraint>(); constrList.add(constr);
//            QuickXplain.constrsUnion(constrList,parentDiag); //keeping a track of removed constraints to construct diagnoses
//            if (!isConsistent(QuickXplain.constrsUnion(bOriginal,cOriginal),model))
//            {
//                bOriginal=new ArrayList<Constraint>(); bOriginal.addAll(b);
//                conflictSet=QuickXplain.quickXPlain(bOriginal,cOriginal,model);
//                if (!conflictSet.isEmpty())// && !FinacialServices.contain(allConflictSets, conflictSet)) // if it's not already been calculated
//                {
//                    nodeConflicts.add(conflictSet);
//                    childC.add(cOriginal);  // saving the constraints set (c) of each child (diagnosis) to be used on the next call.
//                }
//                int index=parentsDiag.indexOf(parentDiag);
//                //constructing diagnoses
//                if (index==-1)
//                    parentsDiag.add(constrList);
//                else
//                    parentsDiag.set(index, constrList); // replacing the diagnoses track with the new one
//            }
//            else if(!FinacialServices.contain(allDiagnoses,constrList) && isMinimal(constrList,allDiagnoses)) //we have a minimal diagnosis
//            {allDiagnoses.add(constrList);
//                long endTime = System.currentTimeMillis();
//                System.out.println("first minimal diagnosis using hiting set is:"+(endTime-startTime) + "ms\n");
//            }
//        }
//        return nodeConflicts;
//    }
//    public static List<List<Constraint>> hsdag(List<Constraint> firstConflictSet, List<Constraint> b, List<Constraint> c, Model model)
//    {
//        List<List<Constraint>> allDiagnoses= new ArrayList<List<Constraint>>();
//        List<List<Constraint>> parentsDiag= new ArrayList<List<Constraint>>();
//        List<Constraint> parentDiag= new ArrayList<Constraint>();
//        List<List<Constraint>> nodeConflicts= new ArrayList<List<Constraint>>();
//        List<List<Constraint>> childCnstnts= new ArrayList<List<Constraint>>();
//        nodeConflictSets(firstConflictSet,b, c, nodeConflicts,model, childCnstnts,parentDiag,parentsDiag, allDiagnoses);
//        while (!nodeConflicts.isEmpty())
//        {
//            List<List<Constraint>> childConflicts= new ArrayList<List<Constraint>>();
//            List<List<Constraint>> childConstraints= new ArrayList<List<Constraint>>();
//            for (int j=0; j<nodeConflicts.size(); j++)
//            {
//                nodeConflictSets(nodeConflicts.get(j), b, childCnstnts.get(j), childConflicts, model, childConstraints,parentsDiag.get(j),parentsDiag, allDiagnoses);
//            }
//            nodeConflicts=childConflicts;
//            childCnstnts=childConstraints;
//        }
//        return allDiagnoses;
//    }
}