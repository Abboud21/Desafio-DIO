package br.com.dio.model;

public record Investment(
        long id,
        long tax,
        long initalFunds)
    {

        @Override
        public String toString() {
            return "Investment{" +
                    "id=" + id +
                    ", tax=" + tax + "%" +
                    ", initalFunds=" + (initalFunds / 100) + "," + (initalFunds % 100) +
                    '}';
        }
    }
