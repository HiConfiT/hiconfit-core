## fma

To easy in programming and testing, I migrated the source code from CECore to CA-CDR-V2, 
and made changes to adapt the code with the generic feature model.

After checking, if you agree with the changes in this branch, I will migrate back the new code of fma from CA-CDR-V2 to CECore.

New and changed classes (not totally sure):

- [ ] AnomalyAwareFeature
- [ ] AnomalyAwareFeatureBuilder
- [ ] ConditionallyDeadAssumptions
- [ ] FalseOptionalAssumptions
- [ ] AssumptionAwareTestCase
- [ ] AnomalyAwareFeatureModelTest

Checklist:

- [ ] Checks the fma package according to the source code in CECore
- [ ] Checks the commented test cases in FMAnalyzerTest
- [ ] Checks the commented codes in FMAnalyzer