package br.uece.lotus.uml.app.runtime.utils.checker;

public enum Template {
    DEFAULT("default"),
    AND_NOT("/\\ ¬"),
    AFTER ("after"),
    /*IN_STEPS ("in steps")*/;

    private String template;

    Template(String template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return this.template;
    }
}
