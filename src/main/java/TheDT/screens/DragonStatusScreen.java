package TheDT.screens;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import TheDT.patches.CurrentScreenEnum;
import TheDT.patches.CustomTags;
import TheDT.screens.screenItems.DragonGrowthCard;
import TheDT.screens.screenItems.DragonStatusScrollButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

import java.util.function.Function;

public class DragonStatusScreen {
	private static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("DragonStatus"));
	public static String[] TEXT = uiStrings.TEXT;

	public DragonGrowthCard[][] dragonGrowthCards;
	public DragonStatusScrollButton[] dragonStatusScrollButtons;

	public float yOffset;
	private float ySpeed;
	public float targetOffset;

	Dragon dragon;
	int curTier;
	int curIndex;
	int availableIndices;

	public boolean needSelection;

	public DragonStatusScreen() {
		dragonGrowthCards = new DragonGrowthCard[4][];
		dragonGrowthCards[1] = new DragonGrowthCard[]{
				new DragonGrowthCard(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, 1, 0, this::onClickCard)
		};
		for (int i = 2; i <= 3; i++) {
			dragonGrowthCards[i] = new DragonGrowthCard[5];
			for (int j = 0; j < 5; j++) {
				dragonGrowthCards[i][j] = new DragonGrowthCard(
						Settings.WIDTH * (14 + 18 * j) / 100.0f, Settings.HEIGHT * (i - 0.5f), i, j, this::onClickCard);
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
		AbstractDungeon.screen = CurrentScreenEnum.DRAGON_GROWTH;
		AbstractDungeon.overlayMenu.showBlackScreen();
		needSelection = false;
		AbstractDungeon.isScreenUp = true;
		AbstractDungeon.overlayMenu.proceedButton.hide();
		AbstractDungeon.overlayMenu.cancelButton.show(TEXT[20]);
		resetPositions();
		if (targetOffset < (curTier - 1) * Settings.HEIGHT) {
			targetOffset = (curTier - 1) * Settings.HEIGHT;
		}
		yOffset = targetOffset;
		ySpeed = 0;
		reopen();
	}

	public void openSelection() {
		needSelection = true;
		AbstractDungeon.overlayMenu.proceedButton.hide();
		AbstractDungeon.overlayMenu.cancelButton.hide();
		resetPositions();
		if (curTier == 1 || curTier == 2) {
			availableIndices = 0;
			for (int i = 1; i < 5; i++) {
				availableIndices <<= 1;
				if (dragonGrowthCards[curTier + 1][i].isClickable()) {
					availableIndices++;
				}
			}
		}
		yOffset = targetOffset = curTier * Settings.HEIGHT;
		ySpeed = 0;
		reopen();
	}

	public void afterDragonGrowth() {
		needSelection = false;
		AbstractDungeon.overlayMenu.proceedButton.setLabel(CombatRewardScreen.TEXT[0]);
		AbstractDungeon.overlayMenu.proceedButton.show();
		AbstractDungeon.overlayMenu.cancelButton.hide();
		resetPositions();
		reopen();
	}

	public void reopen() {
		AbstractDungeon.player.releaseCard();
	}

	public void close() {
		AbstractDungeon.overlayMenu.proceedButton.hide();
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

	public void onClickCard(int tier, int index) {
		if (needSelection) {
			dragon.grow(tier, index, availableIndices);
			afterDragonGrowth();
		}
	}

	public void resetPositions() {
		dragon = DragonTamer.getDragon();
		if (dragon != null) {
			curTier = dragon.getTier();
			curIndex = curTier == 2 ? dragon.tier2Perk : curTier == 3 ? dragon.tier3Perk : 0;
		}
		dragonGrowthCards[curTier][curIndex].moveX(Settings.WIDTH * 0.5f);
		dragonGrowthCards[curTier][curIndex].setIfCurrent(true);

		for (int i = 2; i <= 3; i++) {
			if (i == curTier) {
				continue;
			}
			for (int j = 0; j < 5; j++) {
				dragonGrowthCards[i][j].resetX();
				dragonGrowthCards[i][j].setIfCurrent(false);
			}
		}

		dragonGrowthCards[2][1].setProgress(countDeck(c -> c.type == AbstractCard.CardType.ATTACK), 8);
		dragonGrowthCards[2][2].setProgress(countDeck(c -> c.type == AbstractCard.CardType.SKILL), 8);
		dragonGrowthCards[2][3].setProgress(countDeck(c -> c.tags.contains(CustomTags.DT_BONDING)), 3);
		dragonGrowthCards[2][4].setProgress(countDeck((c) -> c.tags.contains(CustomTags.DT_TACTIC)), 3);
		dragonGrowthCards[3][1].setProgress(countDeck((c) -> c.cardsToPreview instanceof Burn), 5);
		dragonGrowthCards[3][2].setProgress(countDeck((c) -> c.color != CardColorEnum.DT_ORANGE), 4);
		dragonGrowthCards[3][3].setProgress(countDeck((c) -> c.type == AbstractCard.CardType.POWER), 6);
		dragonGrowthCards[3][4].setProgress(AbstractDungeon.player.masterDeck.size(), 30);
		dragonGrowthCards[curTier][curIndex].setProgress(0, 0);
		updateButtons();
	}

	int countDeck(Function<AbstractCard, Boolean> func) {
		int count = 0;
		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (func.apply(c)) {
				count++;
			}
		}
		return count;
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
		if (!needSelection) {
			for (int i = curTier == 2 ? 2 : curTier == 3 ? dragonStatusScrollButtons.length : 0; i < dragonStatusScrollButtons.length; i++) {
				dragonStatusScrollButtons[i].setYOffset(yOffset);
				dragonStatusScrollButtons[i].update();
			}
		} else {
			if (Settings.isControllerMode && !AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.player.viewingRelics) {
				int index = 0;
				boolean anyHovered = false;

				for (; index < dragonGrowthCards[curTier + 1].length; index++) {
					if (dragonGrowthCards[curTier + 1][index].hovered) {
						anyHovered = true;
						break;
					}
				}

				if (!anyHovered) {
					index = 0;
					Gdx.input.setCursorPosition(Settings.WIDTH * 14 / 100, Settings.HEIGHT / 2);
				} else if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
					do {
						++index;
						if (index >= dragonGrowthCards[curTier + 1].length) {
							index = 0;
							break;
						}
					} while (!dragonGrowthCards[curTier + 1][index].isClickable());

					Gdx.input.setCursorPosition(Settings.WIDTH * (14 + 18 * index) / 100, Settings.HEIGHT / 2);
				} else if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
					do {
						++index;
						if (index < 0) {
							index = dragonGrowthCards[curTier + 1].length - 1;
						}
					} while (!dragonGrowthCards[curTier + 1][index].isClickable());

					Gdx.input.setCursorPosition(Settings.WIDTH * (14 + 18 * index) / 100, Settings.HEIGHT / 2);
				}

				if (CInputActionSet.select.isJustPressed()) {
					CInputActionSet.select.unpress();
					onClickCard(curTier + 1, index);
				}
			}
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
		if (!needSelection) {
			for (int i = curTier == 2 ? 2 : curTier == 3 ? dragonStatusScrollButtons.length : 0; i < dragonStatusScrollButtons.length; i++) {
				dragonStatusScrollButtons[i].render(sb);
			}
		}
	}

	private void renderCurrentStatus(SpriteBatch sb) {
		float offset = (curTier - 1) * Settings.HEIGHT - yOffset;
		FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, TEXT[2] + curTier + TEXT[3], 100 * Settings.scale, 0.79f * Settings.HEIGHT + offset, Color.WHITE);
		// Tier explanation
		if (curTier < 3) {
			String inst = TEXT[8] + curTier + TEXT[9] + (curTier + 1) + TEXT[10];
			if (curTier == 2) {
				inst += TEXT[11];
			}
			FontHelper.renderFontLeft(sb, FontHelper.charDescFont, inst, 130 * Settings.scale, 0.64f * Settings.HEIGHT + offset, Color.WHITE);
		}

		// Abilities
		FontHelper.cardTitleFont.getData().setScale(1.0F);
		FontHelper.cardDescFont_L.getData().setScale(1.0F);
		float x1 = Settings.WIDTH / 2.0f + 225 * Settings.scale;
		float x2 = Settings.WIDTH / 2.0f + 250 * Settings.scale;
		FontHelper.renderFontLeft(sb, FontHelper.cardTitleFont, TEXT[12], x1, 750 * Settings.scale + offset, Color.WHITE);
		FontHelper.renderFontLeft(sb, FontHelper.cardTitleFont, TEXT[13] + curTier + TEXT[14], x1, 675 * Settings.scale + offset, Color.WHITE);
		FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, TEXT[15] + curTier + TEXT[16], x2, 625 * Settings.scale + offset, Color.WHITE);
		FontHelper.renderFontLeft(sb, FontHelper.cardTitleFont, TEXT[17], x1, 550 * Settings.scale + offset, Color.WHITE);
		FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, TEXT[18], x2, 500 * Settings.scale + offset, Color.WHITE);
		if (curTier >= 2) {
			FontHelper.renderFontLeft(sb, FontHelper.cardTitleFont, Dragon.dragonGrowthStrings.NAMES[dragon.tier2Perk + 1] + TEXT[19],
					x1, 425 * Settings.scale + offset, Color.WHITE);
			FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, Dragon.dragonGrowthStrings.TEXT[dragon.tier2Perk + 1].replace('\n', ' '),
					x2, 375 * Settings.scale + offset, Color.WHITE);
			if (curTier >= 3) {
				FontHelper.renderFontLeft(sb, FontHelper.cardTitleFont, Dragon.dragonGrowthStrings.NAMES[dragon.tier3Perk + 6] + TEXT[19],
						x1, 300 * Settings.scale + offset, Color.WHITE);
				FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, Dragon.dragonGrowthStrings.TEXT[dragon.tier3Perk + 6].replace('\n', ' '),
						x2, 250 * Settings.scale + offset, Color.WHITE);
			}
		}

		dragonGrowthCards[curTier][curIndex].render(sb);
	}

	private void renderTier(SpriteBatch sb, int tier) {
		float offset = (tier - 1) * Settings.HEIGHT - yOffset;
		if (needSelection) {
			FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, TEXT[6] + tier + TEXT[7], 100 * Settings.scale, 0.79f * Settings.HEIGHT + offset, Color.WHITE);
		} else {
			FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, TEXT[2] + tier + TEXT[4] + (tier - 1) + TEXT[5], 100 * Settings.scale, 0.79f * Settings.HEIGHT + offset, Color.WHITE);
		}
		for (int i = 0; i < 5; i++) {
			dragonGrowthCards[tier][i].render(sb);
		}
	}
}
