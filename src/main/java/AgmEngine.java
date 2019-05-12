import net.sf.tweety.beliefdynamics.BaseRevisionOperator;
import net.sf.tweety.beliefdynamics.DefaultBaseExpansionOperator;
import net.sf.tweety.beliefdynamics.LeviBaseRevisionOperator;
import net.sf.tweety.beliefdynamics.operators.RandomKernelContractionOperator;
import net.sf.tweety.logics.commons.analysis.BeliefSetInconsistencyMeasure;
import net.sf.tweety.logics.pl.analysis.PmInconsistencyMeasure;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.PlBeliefSet;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

public class AgmEngine {

    private PlParser parser = new PlParser();


    public PlBeliefSet revise(PlBeliefSet beliefSet, PropositionalFormula formula) {
        return expand(contract(beliefSet, new Negation(formula)), formula);
    }

    public PlBeliefSet expand(PlBeliefSet beliefSet, PropositionalFormula formula) {
        PlBeliefSet beliefSet1 = new PlBeliefSet();
        beliefSet1.addAll(beliefSet);
        beliefSet1.add(formula);
        return beliefSet1;
    }

    public PlBeliefSet contract(PlBeliefSet beliefSet, PropositionalFormula formula) {
        PlBeliefSet beliefSet1 = new PlBeliefSet();
        BeliefSetInconsistencyMeasure<PropositionalFormula> pm = new PmInconsistencyMeasure();

        beliefSet1.add(formula);
        return beliefSet;
    }


    private static PlBeliefSet reviseBeliefSet(PlBeliefSet beliefSet,
                                               PropositionalFormula formula) {
        BaseRevisionOperator<PropositionalFormula> operator = new LeviBaseRevisionOperator<>(
                new RandomKernelContractionOperator(),
                new DefaultBaseExpansionOperator<>()
        );
        PlBeliefSet newBeliefSet = new PlBeliefSet();
        newBeliefSet.addAll(operator.revise(beliefSet, formula));
        return newBeliefSet;
    }
}
