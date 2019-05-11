import net.sf.tweety.beliefdynamics.BaseRevisionOperator;
import net.sf.tweety.beliefdynamics.DefaultBaseExpansionOperator;
import net.sf.tweety.beliefdynamics.LeviBaseRevisionOperator;
import net.sf.tweety.beliefdynamics.operators.RandomKernelContractionOperator;
import net.sf.tweety.commons.ParserException;
import net.sf.tweety.logics.commons.analysis.BeliefSetInconsistencyMeasure;
import net.sf.tweety.logics.commons.analysis.NaiveMusEnumerator;
import net.sf.tweety.logics.pl.analysis.PmInconsistencyMeasure;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.reasoner.SimpleReasoner;
import net.sf.tweety.logics.pl.sat.PlMusEnumerator;
import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.syntax.PlBeliefSet;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ParserException, IOException {
        PlMusEnumerator.setDefaultEnumerator(new
                NaiveMusEnumerator<>(SatSolver.getDefaultSolver()));
        SatSolver.setDefaultSolver(new Sat4jSolver());
        PlParser parser = new PlParser();
        PlBeliefSet beliefSet = parser.parseBeliefBase("a && b");
        System.out.println("belief set: " + beliefSet);

        SimpleReasoner reasoner = new SimpleReasoner();
        Boolean answer1 = reasoner.query(beliefSet, parser.parseFormula("a && b && c"));

        PlBeliefSet newBeliefSet = reviseBeliefSet(beliefSet, parser.parseFormula("!a"));
        System.out.println("new belief set: " + newBeliefSet);

        PlBeliefSet anotherBeliefSet = reviseBeliefSet(newBeliefSet, parser.parseFormula("a"));
        System.out.println("another belief set: " + anotherBeliefSet);

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
