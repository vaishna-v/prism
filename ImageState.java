import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

public class ImageState {

    private static final int MAX_UNDO = 20;

    private BufferedImage current;
    private BufferedImage original;
    private String fileName = "";

    private final Deque<BufferedImage> undoStack = new ArrayDeque<>();

    public void setImage(BufferedImage img, String name) {
        this.current  = img;
        this.original = img;
        this.fileName = name;
    }

    public void setCurrentImage(BufferedImage img) {
        this.current = img;
    }

    public void pushUndo() {
        if (current == null) return;
        if (undoStack.size() >= MAX_UNDO) undoStack.pollLast();
        BufferedImage copy = new BufferedImage(current.getWidth(), current.getHeight(), current.getType());
        copy.getGraphics().drawImage(current, 0, 0, null);
        undoStack.push(copy);
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            BufferedImage prev = undoStack.pop();
            this.current  = prev;
            this.original = prev;
        }
    }

    public boolean canUndo()  { return !undoStack.isEmpty(); }
    public boolean isEmpty()  { return current == null; }

    public BufferedImage getBufferedImage() { return current; }
    public BufferedImage getOriginalImage() { return original; }
    public Image getFXImage() { return SwingFXUtils.toFXImage(current, null); }
    public String getFileName() { return fileName; }
}
