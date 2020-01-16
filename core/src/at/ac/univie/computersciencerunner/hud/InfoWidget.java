package at.ac.univie.computersciencerunner.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class InfoWidget implements Disposable {

    public Stage stage;
    private Viewport viewPort;

    private Label titleLabel;
    private Label descriptionLabel;

    private boolean currentlyDisplayed = false;

    private Image infoWidget = new Image(new Texture(Gdx.files.internal("infoWidget.png")));

    private static FreeTypeFontGenerator freeTypeFontGenerator;
    private static FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private static BitmapFont font;

    private long infoWidgetOpenedTimestamp;

    public InfoWidget(SpriteBatch spriteBatch) {

        viewPort = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, spriteBatch);

        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("basicFont.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 24;

        fontParameter.color = Color.WHITE;
        font = freeTypeFontGenerator.generateFont(fontParameter);





        Stack stack = new Stack();



        stack.setFillParent(true);
        stack.add(infoWidget);

        stage.addActor(stack);





        Table table = new Table();
        table.setFillParent(true);



        titleLabel = new Label("Title", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(titleLabel).padTop(-130);

        table.row();


        descriptionLabel = new Label("Description", new Label.LabelStyle(font, Color.WHITE));
        descriptionLabel.setWrap(true);




        table.add(descriptionLabel).width(550f).padTop(-50);
        stage.addActor(table);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isCurrentlyDisplayed() {
        return currentlyDisplayed;
    }

    public void setCurrentlyDisplayed(boolean currentlyDisplayed) {
        this.currentlyDisplayed = currentlyDisplayed;
        infoWidgetOpenedTimestamp = System.currentTimeMillis();
    }

    public void setTitleAndDescription(String title, String description) {

        titleLabel.setText(title);
        descriptionLabel.setText(description);

    }

    public long getInfoWidgetOpenedTimestamp() {
        return infoWidgetOpenedTimestamp;
    }

}
