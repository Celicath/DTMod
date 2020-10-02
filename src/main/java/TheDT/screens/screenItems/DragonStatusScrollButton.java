package TheDT.screens.screenItems;

import TheDT.DTModMain;
import basemod.ClickableUIElement;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

public class DragonStatusScrollButton extends ClickableUIElement {
	public static Texture texture = new Texture(DTModMain.makePath("ui/DragonStatusMoveButtonSmall.png"));

	float baseCX;
	float baseCY;
	float cX;
	float cY;
	boolean flipped;
	Runnable callback;

	public DragonStatusScrollButton(float cX, float cY, boolean flipped, Runnable callback) {
		super(texture);

		this.baseCX = this.cX = cX;
		this.baseCY = this.cY = cY;
		this.flipped = flipped;
		this.callback = callback;

		moveX(baseCX);
		setYOffset(0);
	}

	@Override
	protected void onHover() {
		tint.a = 0.5f;
	}

	@Override
	protected void onUnhover() {
		tint.a = 0.0f;
	}

	@Override
	protected void onClick() {
		callback.run();
	}

	public void moveX(float cX) {
		this.cX = cX;
		x = hitbox.x = cX - hb_w / 2.0f;
	}

	public void resetX() {
		moveX(baseCX);
	}

	public void setYOffset(float offset) {
		cY = baseCY - offset;
		y = hitbox.y = cY - hb_h / 2.0f;
	}

	@Override
	public void render(SpriteBatch sb, Color color) {
		sb.setColor(color);
		float halfWidth;
		float halfHeight;
		halfWidth = (float) this.image.getWidth() / 2.0F;
		halfHeight = (float) this.image.getHeight() / 2.0F;
		sb.draw(this.image, this.x - halfWidth + halfWidth * Settings.scale, this.y - halfHeight + halfHeight * Settings.scale, halfWidth, halfHeight, (float) this.image.getWidth(), (float) this.image.getHeight(), Settings.scale, Settings.scale, this.angle, 0, 0, this.image.getWidth(), this.image.getHeight(), false, flipped);
		if (this.tint.a > 0.0F) {
			sb.setBlendFunction(770, 1);
			sb.setColor(this.tint);
			sb.draw(this.image, this.x - halfWidth + halfWidth * Settings.scale, this.y - halfHeight + halfHeight * Settings.scale, halfWidth, halfHeight, (float) this.image.getWidth(), (float) this.image.getHeight(), Settings.scale, Settings.scale, this.angle, 0, 0, this.image.getWidth(), this.image.getHeight(), false, flipped);
			sb.setBlendFunction(770, 771);
		}

		this.renderHitbox(sb);
	}
}
