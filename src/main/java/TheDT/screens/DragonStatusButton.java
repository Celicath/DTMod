package TheDT.screens;

import TheDT.DTModMain;
import TheDT.patches.CurrentScreenEnum;
import TheDT.patches.DragonStatusScreenPatch;
import basemod.TopPanelItem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class DragonStatusButton extends TopPanelItem {
	private static final Texture IMG = new Texture(DTModMain.makePath("ui/DragonStatusButton.png"));
	public static final String ID = DTModMain.makeID("DragonStatusButton");

	public DragonStatusButton() {
		super(IMG, ID);
	}

	@Override
	protected void onClick() {
		if (!CardCrawlGame.isPopupOpen && !(AbstractDungeon.getCurrRoom() instanceof DragonStatusScreenPatch.DragonGrowthRoom)) {
			toggleScreen();
		}
	}

	private static void toggleScreen() {
		if (AbstractDungeon.screen == CurrentScreenEnum.DRAGON_GROWTH) {
			AbstractDungeon.closeCurrentScreen();
		} else {
			if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
				AbstractDungeon.closeCurrentScreen();
				AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
			} else if (!AbstractDungeon.isScreenUp) {
			} else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW) {
				if (AbstractDungeon.previousScreen != null) {
					AbstractDungeon.screenSwap = true;
				}

				AbstractDungeon.closeCurrentScreen();
			} else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.DEATH) {
				AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.DEATH;
				AbstractDungeon.deathScreen.hide();
			} else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.BOSS_REWARD) {
				AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.BOSS_REWARD;
				AbstractDungeon.bossRelicScreen.hide();
			} else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SHOP) {
				AbstractDungeon.overlayMenu.cancelButton.hide();
				AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.SHOP;
			} else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP && !AbstractDungeon.dungeonMapScreen.dismissable) {
				AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.MAP;
			} else if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.SETTINGS && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP) {
				if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.INPUT_SETTINGS) {
					if (AbstractDungeon.previousScreen != null) {
						AbstractDungeon.screenSwap = true;
					}

					AbstractDungeon.closeCurrentScreen();
				} else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD) {
					AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.CARD_REWARD;
					AbstractDungeon.dynamicBanner.hide();
				} else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
					AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.GRID;
					AbstractDungeon.gridSelectScreen.hide();
				} else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT) {
					AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.HAND_SELECT;
				}
			} else {
				if (AbstractDungeon.previousScreen != null) {
					AbstractDungeon.screenSwap = true;
				}

				AbstractDungeon.closeCurrentScreen();
			}
			if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.VICTORY) {
				DTModMain.dragonStatusScreen.open();
			}
		}
		InputHelper.justClickedLeft = false;
	}

	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);
		if (hitbox.hovered) {
			float x = 1550.0F * Settings.scale;
			float y = (float) Settings.HEIGHT - 120.0F * Settings.scale;
			TipHelper.renderGenericTip(x, y, DragonStatusScreen.TEXT[0], DragonStatusScreen.TEXT[1]);
		}
	}
}
