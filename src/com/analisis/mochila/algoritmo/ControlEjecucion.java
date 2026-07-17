package com.analisis.mochila.algoritmo;

public final class ControlEjecucion {
    private boolean pausado;
    private boolean cancelado;
    private int demoraMilisegundos;

    public ControlEjecucion(int demoraMilisegundos) {
        this.demoraMilisegundos = demoraMilisegundos;
    }

    public synchronized void setPausado(boolean pausado) {
        this.pausado = pausado;
        notifyAll();
    }

    public synchronized boolean isPausado() {
        return pausado;
    }

    public synchronized void cancelar() {
        cancelado = true;
        pausado = false;
        notifyAll();
    }

    public synchronized boolean isCancelado() {
        return cancelado;
    }

    public synchronized void setDemoraMilisegundos(int demoraMilisegundos) {
        this.demoraMilisegundos = Math.max(0, demoraMilisegundos);
    }

    public void esperarPaso() {
        synchronized (this) {
            while (pausado && !cancelado) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    cancelado = true;
                    return;
                }
            }
        }

        int demora;
        synchronized (this) {
            demora = demoraMilisegundos;
        }
        if (!cancelado && demora > 0) {
            try {
                Thread.sleep(demora);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                synchronized (this) {
                    cancelado = true;
                }
            }
        }
    }
}
