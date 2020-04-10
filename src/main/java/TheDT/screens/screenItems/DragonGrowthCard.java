package TheDT.screens.screenItems;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import basemod.ClickableUIElement;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;

public class DragonGrowthCard extends ClickableUIElement {
	public static Texture growthCardTexture = new Texture(DTModMain.makePath("ui/GrowthCard.png"));

	public static Texture growthCardDisabledTexture = new Texture(DTModMain.makePath("ui/GrowthCardDisabled.png"));
	public static Texture growthCardFrameTexture = new Texture(DTModMain.makePath("ui/GrowthCardFrame.png"));
	public static final int CARD_WIDTH = 320;
	public static final int CARD_HEIGHT = 480;
	public static final int FRAME_WIDTH = 256;
	public static final int FRAME_HEIGHT = 256;
	public static final int FRAME_OFFSET = 80;
	public static final int DRAGON_WIDTH = 512;
	public static final int DRAGON_HEIGHT = 512;

	float frameScale;
	float uiScale;
	float baseCX;
	float baseCY;
	float cX;
	float cY;
	Color uiColor;
	int dragonTier;
	int dragonIndex;
	boolean curTier;

	public static final Color uiColorHover = Color.WHITE.cpy();
	public static final Color uiColorUnhover = new Color(0.8f, 0.8f, 0.8f, 0.9f);

	static {
		growthCardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		growthCardDisabledTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
	}

	public DragonGrowthCard(float cX, float cY, int dragonTier, int dragonIndex) {
		super(growthCardTexture);

		this.dragonTier = dragonTier;
		this.dragonIndex = dragonIndex;
		this.baseCX = this.cX = cX;
		this.baseCY = this.cY = cY;

		moveX(baseCX);
		setYOffset(0);

		uiScale = 1.0f;
		uiColor = uiColorUnhover;
	}

	@Override
	protected void onHover() {
		uiScale = MathHelper.fadeLerpSnap(uiScale, 1.025F);
		uiColor = uiColorHover;
	}

	@Override
	protected void onUnhover() {
		uiScale = MathHelper.cardScaleLerpSnap(uiScale, 1.0F);
		uiColor = uiColorUnhover;
	}

	@Override
	protected void onClick() {

	}

	public void moveX(float cX) {
		this.cX = cX;
		x = hitbox.x = cX - hb_w / 2.0f;
		curTier = true;
	}

	public void resetX() {
		moveX(baseCX);
		curTier = false;
	}

	public void setCurrent(boolean curTier) {
		this.curTier = curTier;
		if (curTier) {
			frameScale = 0.75f;
		} else {
			frameScale = 0.5f;
		}
	}

	public void setYOffset(float offset) {
		cY = baseCY - offset;
		y = hitbox.y = cY - hb_h / 2.0f;
	}

	@Override
	public void render(SpriteBatch sb) {
		float scale = Settings.scale;
		float frameScale = Settings.scale * this.frameScale;
		float dragonScale = frameScale * 224 / 256;
		sb.setColor(uiColor);
		sb.draw(isClickable() ? growthCardTexture : growthCardDisabledTexture,
				cX - CARD_WIDTH / 2.0f,
				cY - CARD_HEIGHT / 2.0f,
				CARD_WIDTH / 2.0f,
				CARD_HEIGHT / 2.0f,
				CARD_WIDTH, CARD_HEIGHT, scale * uiScale, scale * uiScale, 0.0F, 0, 0, CARD_WIDTH, CARD_HEIGHT, false, false);
		sb.setColor(Color.WHITE);
		sb.draw(growthCardFrameTexture,
				cX - FRAME_WIDTH / 2.0f,
				cY - FRAME_HEIGHT / 2.0f + (curTier ? 0 : FRAME_OFFSET * frameScale),
				FRAME_WIDTH / 2.0f,
				FRAME_HEIGHT / 2.0f,
				FRAME_WIDTH, FRAME_HEIGHT, frameScale, frameScale, 0.0F, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, false, false);
		sb.draw(Dragon.imgs[dragonTier][dragonIndex],
				cX - DRAGON_WIDTH / 2.0f,
				cY - DRAGON_HEIGHT * 0.25f + (curTier ? 0 : FRAME_OFFSET * frameScale),
				DRAGON_WIDTH / 2.0f,
				DRAGON_HEIGHT * 0.25f,
				DRAGON_WIDTH, DRAGON_HEIGHT, dragonScale, dragonScale, 0.0F, 0, 0, DRAGON_WIDTH, DRAGON_HEIGHT, false, false);

		int index = dragonTier == 1 ? 0 : dragonTier * 5 + dragonIndex - 9;
		FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, Dragon.dragonGrowthStrings.NAMES[index],
				cX, cY + CARD_HEIGHT * 0.34f, Color.YELLOW);

		if (!curTier) {
			FontHelper.renderFontCentered(sb, FontHelper.topPanelAmountFont, Dragon.dragonGrowthStrings.TEXT[index + 10],
					cX, cY + CARD_HEIGHT * 0.25f, Color.LIGHT_GRAY);
			FontHelper.renderFontCentered(sb, FontHelper.cardDescFont_L, Dragon.dragonGrowthStrings.TEXT[index],
					cX, cY - CARD_HEIGHT * 0.2f, Color.WHITE);
		}

		renderHitbox(sb);
	}
}
