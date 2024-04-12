class Block {
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    private String scheduleName;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;

    // 생성자, Getter 및 Setter 생략

    @Override
    public String toString() {
        return "Block{" +
                "scheduleName='" + scheduleName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endDate='" + endDate + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}