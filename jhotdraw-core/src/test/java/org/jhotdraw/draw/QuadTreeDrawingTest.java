package org.jhotdraw.draw;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
import java.util.List;
import org.jhotdraw.draw.action.ArrangeActionType;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuadTreeDrawingTest {
    private QuadTreeDrawing drawing;

    public QuadTreeDrawingTest() {
    }

    @Before
    public void setUp() {
        this.drawing = new QuadTreeDrawing();
    }

    @Test
    public void testArrangeBringToFront() {
        Figure figure1 = new RectangleFigure();
        Figure figure2 = new RectangleFigure();
        Figure figure3 = new RectangleFigure();
        this.drawing.basicAdd(0, figure1);
        this.drawing.basicAdd(1, figure2);
        this.drawing.basicAdd(2, figure3);
        this.drawing.arrange(figure2, ArrangeActionType.BRING_TO_FRONT);
        List<Figure> children = this.drawing.getChildren();
        Assert.assertEquals("Figure 2 should be at the front", figure2, children.get(2));
        Assert.assertEquals("Figure 1 should be 3rd", figure1, children.get(0));
        Assert.assertEquals("Figure 3 should be 2nd", figure3, children.get(1));
    }

    @Test
    public void testArrangeSendToBack() {
        Figure figure1 = new RectangleFigure();
        Figure figure2 = new RectangleFigure();
        Figure figure3 = new RectangleFigure();
        this.drawing.basicAdd(0, figure1);
        this.drawing.basicAdd(1, figure2);
        this.drawing.basicAdd(2, figure3);
        this.drawing.arrange(figure2, ArrangeActionType.SEND_TO_BACK);
        List<Figure> children = this.drawing.getChildren();
        Assert.assertEquals("Figure 2 should be at the back", figure2, children.get(0));
        Assert.assertEquals("Figure 1 should be 2nd", figure1, children.get(1));
        Assert.assertEquals("Figure 3 should be 1st", figure3, children.get(2));
    }

    @Test
    public void testArrangeAlreadyAtFront() {
        Figure figure1 = new RectangleFigure();
        Figure figure2 = new RectangleFigure();
        this.drawing.basicAdd(0, figure1);
        this.drawing.basicAdd(1, figure2);
        this.drawing.arrange(figure1, ArrangeActionType.BRING_TO_FRONT);
        List<Figure> children = this.drawing.getChildren();
        Assert.assertEquals("Figure 1 should remain at the front", figure1, children.get(1));
        Assert.assertEquals("Figure 2 should remain at the back", figure2, children.get(0));
    }
}
