package de.ctoffer.commons.robot;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class BaseBot {
    private static final Random random = new Random();
    private final Robot robot;
    private int lastPositionX;
    private int lastPositionY;

    public BaseBot() {
        try {
            this.robot = new Robot();
        } catch (final AWTException awtException) {
            throw new RuntimeException(awtException);
        }

        final var point = MouseInfo.getPointerInfo().getLocation();
        this.lastPositionX = (int) point.getX();
        this.lastPositionY = (int) point.getY();
    }

    public void scrollDown(int ticks) {
        for (int i = 0; i < ticks; ++i) {
            robot.mouseWheel(1);
            sleepWithDelta(50, 10);
        }
    }

    public void scrollUp(int ticks) {
        for (int i = 0; i < ticks; ++i) {
            robot.mouseWheel(-1);
            sleepWithDelta(50, 10);
        }
    }

    public void moveCursorTo(final int x, final int y) {
        moveCursorTo(x, y, 0);
    }

    public void moveCursorTo(final int x, final int y, final int deviation) {
        moveCursorTo(x, y, deviation, deviation);
    }

    public void moveCursorTo(final int x, final int y, final int deviationX, final int deviationY) {
        var origin = MouseInfo.getPointerInfo().getLocation();


        final var nextX = deviationX == 0 ? x : x + randomInt(-deviationX, deviationX);
        final var nextY = deviationY == 0 ? y :y + randomInt(-deviationY, deviationY);

        var deltaX = nextX - origin.x;
        var deltaY = nextY - origin.y;
        double distance = Math.abs(deltaX) + Math.abs(deltaY);

        for (int step = 0; step < distance; step += 2) {
            robot.mouseMove(
                    origin.x + (int) (deltaX * step / distance),
                    origin.y + (int) (deltaY * step / distance)
            );
            sleepNoThrow(1);
        }
        this.lastPositionX = nextX;
        this.lastPositionY = nextY;
    }

    public void moveRelativeVertical(int distance, int delta) {
        final var nextY = lastPositionY + randomAround(distance, delta);
        robot.mouseMove(lastPositionX, nextY);
        this.lastPositionY = nextY;
    }

    public void moveRelativeHorizontal(int distance, int delta) {
        final var nextX = lastPositionX + randomAround(distance, delta);
        robot.mouseMove(nextX, lastPositionY);
        this.lastPositionX = nextX;
    }


    public void leftClick() {
        click(InputEvent.BUTTON1_DOWN_MASK);
    }

    private void click(final int button) {
        robot.mousePress(button);
        sleepNoThrow(200);
        robot.mouseRelease(button);
    }

    public void rightClick() {
        click(InputEvent.BUTTON3_DOWN_MASK);
    }

    public void pressKey(final int keyCode, final int millis, final int delta) {
        robot.keyPress(keyCode);
        sleepWithDelta(millis, delta);
        robot.keyRelease(keyCode);
    }

    public static void sleepWithDelta(final int millis, final int delta) {
        sleepNoThrow(randomAround(millis, delta));
    }

    private static int randomAround(int center, int delta) {
        return randomInt(center - delta, center + delta);
    }

    private static int randomInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static void sleepNoThrow(long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

    public void pause(int millis, int delta) {
        sleepNoThrow(randomAround(millis, delta));
    }

    public void type(final String sequence) {
        for (final char c : sequence.toCharArray()) {
            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }

            pressKey(Character.toUpperCase(c), 500, 5);

            if (Character.isUpperCase(c)) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }
        }
    }

    public BufferedImage createScreenShot(int positionX, int positionY, int width, int height) {
        return this.robot.createScreenCapture(new Rectangle(positionX, positionY, width, height));
    }
}
