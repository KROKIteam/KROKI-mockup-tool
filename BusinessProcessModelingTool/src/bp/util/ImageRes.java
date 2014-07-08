package bp.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum ImageRes {

    SYSTEM_TASK("src/bp/resources/image/system.png"),
    USER_TASK("src/bp/resources/image/user.png"),
    TIMER("src/bp/resources/image/timer.png"),
    CONDITION("src/bp/resources/image/condition.png"),
    MESSAGE("src/bp/resources/image/message.jpg"),
    SIGNAL("src/bp/resources/image/signal.png"),
    ERROR("src/bp/resources/image/error.png"),
    LINK("src/bp/resources/image/link.png");

    private final String path;
    private Image img;

    private ImageRes(final String path) {
        this.path = path;
        initImage();
    }

    public String getPath() {
        return this.path;
    }

    private void initImage() {
        try {
            this.img = ImageIO.read(new File(this.path));
        } catch (final IOException e) {
            this.img = null;
            e.printStackTrace();
        }
    }

    public Image getImage() {
        return this.img;
    }
}
