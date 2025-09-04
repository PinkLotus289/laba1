package demo.parallel;

import java.awt.Color;

public final class Palette {
    private Palette() {}

    /**
     * Красим пиксель по числу итераций.
     * @param iter    сколько итераций сделано до выхода
     * @param maxIter максимум итераций
     * @param zAbs2   квадрат модуля z в момент выхода (|z|^2), если есть — для сглаживания; иначе можно передать -1
     */
    public static int rgb(int iter, int maxIter, double zAbs2) {
        if (iter >= maxIter) {
            return 0xFF000000; // чёрный, точки множества
        }

        // ----- сглаживание (необязательно, но красиво) -----
        double smooth = 0.0;
        if (zAbs2 > 0) {
            // mu = iter - log(log|z|)/log 2
            double mu = iter - Math.log(Math.log(Math.sqrt(zAbs2))) / Math.log(2.0);
            smooth = Math.max(0, Math.min(1, mu - Math.floor(mu)));
        }

        double t = (iter + smooth) / (double) maxIter; // 0..1
        // HSB-палитра: от синего к жёлтому → фиолетовым
        float h = (float) (0.70 - 0.70 * t); // 0.70≈синяя зона
        float s = 0.90f;
        float b = (float) (0.25 + 0.75 * t);

        return Color.HSBtoRGB(h, s, b); // возвращает ARGB (alpha=255)
    }
}
