package br.uece.lotus.msc.app.runtime.probabilisticReach;

import br.uece.lotus.Component;

public abstract class ProbabilisticReachAlgorithmStrategy {

    public double probabilityBetween(Component parallelComponet, Integer sourceId, Integer targetId) {

        return 0;
    }

    public double probabilityBetween(Component parallelComponet, Integer source, Integer target, Integer steps, Integer actionTargetID) {

        return 0;
    }
}
