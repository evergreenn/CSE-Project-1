class Todo {
    private String scheduleName;

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    private String startDate;

    // 생성자, Getter 및 Setter 생략

    @Override
    public String toString() {
        return "Todo{" +
                "scheduleName='" + scheduleName + '\'' +
                ", startDate='" + startDate + '\'' +
                '}';
    }
}