package com.example.game;

public class RankUser {
        private String userId;
        private int level;
        private double time;

        public RankUser(String userId, int level, double time) {
            this.userId = userId;
            this.level = level;
            this.time = time;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }
}
