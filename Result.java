public enum Result {
    ABSORBED("absorbed"), REFLECTION("reflection"), DETOUR("detour");

    public final String label;

    private Result(String label) {
        this.label = label;
    }
}