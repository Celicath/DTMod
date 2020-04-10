package TheDT.screens;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CurrentScreenEnum;
import TheDT.screens.screenItems.DragonGrowthCard;
import TheDT.screens.screenItems.DragonStatusScrollButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

public class DragonStatusScreen {
	private static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("DragonStatus"));
	public static String[] TEXT = uiStrings.TEXT;

	public DragonGrowthCard[][] dragonGrowthCards;
	public DragonStatusScrollButton[] dragonStatusScrollButtons;

	private float yOffset;
	private float ySpeed;
	private float targetOffset;

	Dragon dragon;
	int curTier;
	int curIndex;

	public DragonStatusScreen() {
		dragonGrowthCards = new DragonGrowthCard[4][];
		dragonGrowthCards[1] = new DragonGrowthCard[]{
				new DragonGrowthCard(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, 1, 0)
		};
		for (int i = 2; i <= 3; i++) {
			dragonGrowthCards[i] = new DragonGrowthCard[5];
			for (int j = 0; j < 5; j++) {
				dragonGrowthCards[i][j] = new DragonGrowthCard(
						Settings.WIDTH * (14 + 18 * j) / 100.0f, Settings.HEIGHT * (i - 0.5f), i, j);
			}
		}
		dragonStatusScrollButtons = new DragonStatusScrollButton[]{
				new DragonStatusScrollButton(Settings.WIDTH / 2.0f, Settings.HEIGHT * 0.8f, true, () -> targetOffset = Settings.HEIGHT),
				new DragonStatusScrollButton(Settings.WIDTH / 2.0f, Settings.HEIGHT * 1.2f, false, () -> targetOffset = 0),
				new DragonStatusScrollButton(Settings.WIDTH / 2.0f, Settings.HEIGHT * 1.8f, true, () -> targetOffset = Settings.HEIGHT * 2),
				new DragonStatusScrollButton(Settings.WIDTH / 2.0f, Settings.HEIGHT * 2.2f, false, () -> targetOffset = Settings.HEIGHT)
		};
	}

	public void open() {
		AbstractDungeon.player.releaseCard();
		AbstractDungeon.screen = CurrentScreenEnum.DRAGON_GROWTH;
		AbstractDungeon.overlayMenu.showBlackScreen();
		AbstractDungeon.overlayMenu.proceedButton.hide();
		AbstractDungeon.overlayMenu.cancelButton.show(TEXT[17]);
		AbstractDungeon.isScreenUp = true;

		dragon = DragonTamer.getDragon();
		if (dragon != null) {
			curTier = dragon.getTier();
			curIndex = curTier == 2 ? dragon.tier2Perk : curTier == 3 ? dragon.tier3Perk : 0;
			yOffset = targetOffset = (curTier - 1) * Settings.HEIGHT;
		}
		resetPositions();
		ySpeed = 0;
	}

	public void close() {
		AbstractDungeon.overlayMenu.cancelButton.hide();
	}

	public void update() {
		float maxSpeed = Math.min(Settings.HEIGHT * 6, Math.max(Math.abs(yOffset - targetOffset) * 8, Settings.HEIGHT * 0.1f));
		ySpeed = Math.min(maxSpeed, ySpeed + Settings.HEIGHT * Gdx.graphics.getDeltaTime() * 12);
		if (yOffset > targetOffset) {
			yOffset = Math.max(yOffset - ySpeed * Gdx.graphics.getDeltaTime(), targetOffset);
		} else if (yOffset < targetOffset) {
			yOffset = Math.min(yOffset + ySpeed * Gdx.graphics.getDeltaTime(), targetOffset);
		}

		updateButtons();
	}

	public void resetPositions() {
		dragonGrowthCards[curTier][curIndex].moveX(Settings.WIDTH * 0.4f);
		dragonGrowthCards[curTier][curIndex].setCurrent(true);
		for (int i = 2; i <= 3; i++) {
			if (i == curTier) {
				continue;
			}
			for (int j = 0; j < 5; j++) {
				dragonGrowthCards[i][j].resetX();
				dragonGrowthCards[i][j].setCurrent(false);
			}
		}
		updateButtons();
	}

	public void updateButtons() {
		dragonGrowthCards[curTier][curIndex].setYOffset(yOffset);
		if (curTier == 1) {
			dragonGrowthCards[curTier][curIndex].update();
		}
		for (int i = 2; i <= 3; i++) {
			for (int j = 0; j < 5; j++) {
				if (i == curTier && j != curIndex) {
					dragonGrowthCards[i][j].setYOffset(Settings.HEIGHT * 5);
				} else {
					dragonGrowthCards[i][j].setYOffset(yOffset);
					dragonGrowthCards[i][j].update();
				}
			}
		}
		for (int i = curTier == 2 ? 1 : curTier == 3 ? dragonStatusScrollButtons.length : 0; i < dragonStatusScrollButtons.length; i++) {
			dragonStatusScrollButtons[i].setYOffset(yOffset);
			dragonStatusScrollButtons[i].update();
		}
	}

	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE);

		switch (curTier) {
			case 1:
				renderCurrentStatus(sb);
				renderTier(sb, 2);
				renderTier(sb, 3);
				break;
			case 2:
				renderCurrentStatus(sb);
				renderTier(sb, 3);
				break;
			case 3:
				renderCurrentStatus(sb);
				break;
		}
		for (int i = curTier == 2 ? 1 : curTier == 3 ? dragonStatusScrollButtons.length : 0; i < dragonStatusScrollButtons.length; i++) {
			dragonStatusScrollButtons[i].render(sb);
		}
	}

	private void renderCurrentStatus(SpriteBatch sb) {
		float offset = (curTier - 1) * Settings.HEIGHT - yOffset;
		FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, TEXT[2] + curTier + TEXT[3], 100 * Settings.scale, 0.79f * Settings.HEIGHT + offset, Color.WHITE);

		// Tier explanation
		if (curTier < 3) {
			FontHelper.renderFontLeft(sb, FontHelper.charDescFont, TEXT[6] + curTier + TEXT[7] + (curTier + 1) + TEXT[8], 150 * Settings.scale, 0.6f * Settings.HEIGHT + offset, Color.WHITE);
		}

		// Abilities
		FontHelper.renderFontLeft(sb, FontHelper.cardTitleFont, TEXT[9], 975 * Settings.scale, 750 * Settings.scale + offset, Color.WHITE);
		FontHelper.renderFontLeft(sb, FontHelper.cardTitleFont, TEXT[10] + curTier + TEXT[11], 975 * Settings.scale, 675 * Settings.scale + offset, Color.WHITE);
		FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, TEXT[12] + curTier + TEXT[13], 1000 * Settings.scale, 625 * Settings.scale + offset, Color.WHITE);
		FontHelper.renderFontLeft(sb, FontHelper.cardTitleFont, TEXT[14], 975 * Settings.scale, 550 * Settings.scale + offset, Color.WHITE);
		FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, TEXT[15], 1000 * Settings.scale, 500 * Settings.scale + offset, Color.WHITE);
		if (curTier >= 2) {
			FontHelper.renderFontLeft(sb, FontHelper.cardTitleFont, Dragon.dragonGrowthStrings.NAMES[curIndex + 1] + TEXT[16],
					975 * Settings.scale, 425 * Settings.scale + offset, Color.WHITE);
			FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, Dragon.dragonGrowthStrings.TEXT[curIndex + 1],
					1000 * Settings.scale, 375 * Settings.scale + offset, Color.WHITE);
			if (curTier >= 3) {
				FontHelper.renderFontLeft(sb, FontHelper.cardTitleFont, Dragon.dragonGrowthStrings.NAMES[curIndex + 6] + TEXT[16],
						975 * Settings.scale, 300 * Settings.scale + offset, Color.WHITE);
				FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, Dragon.dragonGrowthStrings.TEXT[curIndex + 6],
						1000 * Settings.scale, 250 * Settings.scale + offset, Color.WHITE);
			}
		}

		dragonGrowthCards[curTier][curIndex].render(sb);
	}

	private void renderTier(SpriteBatch sb, int tier) {
		float offset = (tier - 1) * Settings.HEIGHT - yOffset;
		FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, TEXT[2] + tier + TEXT[4] + (tier - 1) + TEXT[5], 100 * Settings.scale, 0.79f * Settings.HEIGHT + offset, Color.WHITE);

		for (int i = 0; i < 5; i++) {
			dragonGrowthCards[tier][i].render(sb);
		}
	}
}
