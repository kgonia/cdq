package com.cdq.demo.task;

public class Task {

    private final String pattern;

    private final String input;
    private final long delayInMillis;

    private int bestPosition = -1;

    private int minTypos = Integer.MAX_VALUE;

    private TaskStatus taskStatus;

    private double progressPercentage = 0.0;

    Task(String pattern, String input) {
        this(pattern, input, 2500);
    }

    Task(String pattern, String input, long delayInMillis) {
        this.pattern = pattern;
        this.input = input;
        this.delayInMillis = delayInMillis;
    }

    void run() {
        int inputLength = input.length();
        int patternLength = pattern.length();

        char[] patternChars = pattern.toCharArray();

        int totalIterations = inputLength - patternLength;

        int bestPosition = -1;
        int minTypos = Integer.MAX_VALUE;

        for(int i =0; i <= totalIterations; i++){
            char[] chars = input.substring(i, i + pattern.length()).toCharArray();

            int typos = 0;
            for (int j =0; j < chars.length; j++){
                if(chars[j] != patternChars[j]){
                    typos++;
                }
            }

            if (typos < minTypos){
                bestPosition = i;
                minTypos = typos;
            }

            this.progressPercentage = (double) i / totalIterations * 100;

            try {
                Thread.sleep(this.delayInMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.progressPercentage = 100;

        this.bestPosition = bestPosition;
        this.minTypos = minTypos;
    }

    public int getBestPosition(){
        return bestPosition;
    }

    public int getMinTypos(){
        return minTypos;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }
}
