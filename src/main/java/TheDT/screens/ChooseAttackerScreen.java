package TheDT.screens;

import TheDT.DTModMain;
import TheDT.actions.ChooseAttackerAction;
import TheDT.cards.AbstractDTCard;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.utils.TargetArrow;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.util.List;
import java.util.stream.Collectors;

public class ChooseAttackerScreen {
	private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("ChooseAttackerScreen")).TEXT;

	private static AbstractCreature hoveredCreature;
	private static AbstractCreature firstHoveredCreature;
	private static float arrowTime;
	private static final Hitbox tmpHitbox = new Hitbox(0, 0);
	private static List<AbstractMonster> livingMonsters;

	public static void open(CardRewardScreen screen) {
		screen.rItem = null;
		hoveredCreature = null;
		arrowTime = 0.0f;
		AbstractDungeon.isScreenUp = true;
		AbstractDungeon.screen = AbstractDungeon.CurrentScreen.CARD_REWARD;
		AbstractDungeon.dynamicBanner.appear(TEXT[0]);
		AbstractDungeon.overlayMenu.showBlackScreen(0.5f);

		firstHoveredCreature = AbstractDungeon.getMonsters().hoveredMonster;

		livingMonsters = AbstractDungeon.getCurrRoom().monsters.monsters.stream().filter(m -> !m.isDying && !m.isEscaping).collect(Collectors.toList());
	}

	public static void update() {
		if (AbstractDungeon.player.cardInUse != null) {
			AbstractDungeon.player.cardInUse.update();
		}

		AbstractDungeon.player.update();
		for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!m.isDeadOrEscaped()) {
				m.update();
			}
		}

		if (Settings.isControllerMode && !AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.player.viewingRelics) {
			int index = 0;
			boolean anyHovered = false;

			for (AbstractCreature c : ChooseAttackerAction.activeThis.attackers) {
				if (c.hb.hovered) {
					anyHovered = true;
					break;
				}
				index++;
			}

			if (!anyHovered) {
				index = 0;
				Gdx.input.setCursorPosition((int) ChooseAttackerAction.activeThis.attackers.get(index).hb.cX, Settings.HEIGHT - (int) ChooseAttackerAction.activeThis.attackers.get(index).hb.cY);
			} else if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
				++index;
				if (index > ChooseAttackerAction.activeThis.attackers.size() - 1) {
					index = 0;
				}

				Gdx.input.setCursorPosition((int) ChooseAttackerAction.activeThis.attackers.get(index).hb.cX, Settings.HEIGHT - (int) ChooseAttackerAction.activeThis.attackers.get(index).hb.cY);
			} else if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
				--index;
				if (index < 0) {
					index = ChooseAttackerAction.activeThis.attackers.size() - 1;
				}

				Gdx.input.setCursorPosition((int) ChooseAttackerAction.activeThis.attackers.get(index).hb.cX, Settings.HEIGHT - (int) ChooseAttackerAction.activeThis.attackers.get(index).hb.cY);
			}
		}

		AbstractCreature creature = null;
		for (AbstractCreature c : ChooseAttackerAction.activeThis.attackers) {
			c.hb.update();
			if (c.hb.hovered) {
				creature = c;
			}
		}
		if (creature != null && creature == firstHoveredCreature) {
			if (arrowTime < 1.0f) {
				creature = null;
			} else {
				firstHoveredCreature = null;
			}
		} else {
			firstHoveredCreature = null;
		}

		if (creature != null && InputHelper.justClickedLeft) {
			creature.hb.clickStarted = true;
		}

		if (creature != null && CInputActionSet.select.isJustPressed()) {
			CInputActionSet.select.unpress();
			creature.hb.clicked = true;
		}

		if (creature != null && (creature.hb.clicked || CInputActionSet.select.isJustPressed())) {
			creature.hb.clicked = false;
			ChooseAttackerAction.activeThis.choseAttacker(creature);

			AbstractDungeon.closeCurrentScreen();
		}

		arrowTime += Gdx.graphics.getDeltaTime();
		if (hoveredCreature != creature) {
			hoveredCreature = creature;
			arrowTime = 0.0f;
		}
	}

	static void renderCreature(SpriteBatch sb, AbstractCreature c) {
		if (ReflectionHacks.getPrivate(c, AbstractCreature.class, "atlas") != null) {
			c.renderHealth(sb);
			sb.end();
			CardCrawlGame.psb.begin();
			AbstractCreature.sr.draw(CardCrawlGame.psb, (Skeleton) ReflectionHacks.getPrivate(c, AbstractCreature.class, "skeleton"));
			CardCrawlGame.psb.end();
			sb.begin();
		} else {
			if (c instanceof DragonTamer) {
				((DragonTamer) c).renderOnlyDT(sb);
			} else {
				c.render(sb);
			}
		}
	}

	static String damageString(AbstractDTCard card, AbstractCreature attacker, AbstractMonster target, int index) {
		int baseDamage;
		int damage;
		if (attacker instanceof AbstractPlayer) {
			if (attacker instanceof Dragon) {
				baseDamage = card.dtBaseDragonDamage;
				damage = index == -1 ? card.dtDragonDamage : card.dragonMultiDamage[index];
			} else {
				baseDamage = card.baseDamage;
				damage = index == -1 ? card.damage : card.multiDamage[index];
			}
		} else {
			int[] baseDamageArray = new int[]{card.baseDamage};
			if (index == -1) {
				damage = ChooseAttackerAction.activeThis.dtCard.calculateCardDamageAsMonster(attacker, baseDamageArray, target, null);
				baseDamage = baseDamageArray[0];
			} else {
				int[] enemyMultiDamage = new int[AbstractDungeon.getCurrRoom().monsters.monsters.size()];
				ChooseAttackerAction.activeThis.dtCard.calculateCardDamageAsMonster(attacker, baseDamageArray, target, enemyMultiDamage);
				damage = enemyMultiDamage[index];
				baseDamage = baseDamageArray[0];
			}
		}
		String result;
		if (damage > baseDamage) {
			result = "[#7fff00]" + damage + "[]";
		} else if (damage < baseDamage) {
			result = "[#ff6563]" + damage + "[]";
		} else {
			result = Integer.toString(damage);
		}
		return TEXT[2] + result + TEXT[3];
	}

	public static void render(SpriteBatch sb) {
		if (hoveredCreature != null) {
			renderCreature(sb, hoveredCreature);

			String text = ChooseAttackerAction.activeThis.caCard.hoverText(hoveredCreature, ChooseAttackerAction.activeThis.m);

			if (ChooseAttackerAction.activeThis.isAttack || !(hoveredCreature instanceof AbstractPlayer)) {
				hoveredCreature.renderReticle(sb);
			}

			if (ChooseAttackerAction.activeThis.m != null) {
				renderCreature(sb, ChooseAttackerAction.activeThis.m);

				if (ChooseAttackerAction.activeThis.isAttack) {
					ChooseAttackerAction.activeThis.dtCard.calculateCardDamage(ChooseAttackerAction.activeThis.m);
					TargetArrow.drawTargetArrow(
							sb, hoveredCreature.hb, ChooseAttackerAction.activeThis.m.hb, TargetArrow.CONTROL_HEIGHT * Settings.scale, arrowTime, 1.0f,
							damageString(ChooseAttackerAction.activeThis.dtCard, hoveredCreature, ChooseAttackerAction.activeThis.m, -1));
				}
			} else {
				if (ChooseAttackerAction.activeThis.isAttack) {
					int index = 0;
					int size = livingMonsters.size();
					float offset = -0.5f - size * 0.5f;
					if (size >= 5) {
						offset = -2.5f;
					}
					ChooseAttackerAction.activeThis.dtCard.calculateCardDamage(ChooseAttackerAction.activeThis.m);
					for (AbstractMonster m : livingMonsters) {
						if (!m.isDeadOrEscaped()) {
							renderCreature(sb, m);
							TargetArrow.drawTargetArrow(
									sb, hoveredCreature.hb, m.hb, -TargetArrow.CONTROL_HEIGHT * Settings.scale * (index + offset), arrowTime, 1.0f,
									damageString(ChooseAttackerAction.activeThis.dtCard, hoveredCreature, m, index));
						}
						index++;
					}
				}
			}

			if (text != null) {
				FontHelper.renderFontCentered(
						sb,
						FontHelper.panelNameFont,
						text,
						hoveredCreature.hb.cX,
						hoveredCreature.hb.cY,
						Color.WHITE.cpy());
			}
		} else {
			if (AbstractDungeon.player.cardInUse != null) {
				AbstractDungeon.player.cardInUse.render(sb);
			}
			for (AbstractCreature c : ChooseAttackerAction.activeThis.attackers) {
				if (c instanceof DragonTamer || c instanceof Dragon) {
					tmpHitbox.resize(c.hb.width * 0.75f, c.hb.height * 0.75f);
					tmpHitbox.move(c.hb.cX, c.hb.cY);
					c.renderReticle(sb, tmpHitbox);
				} else {
					c.renderReticle(sb, c.hb);
				}
				if (arrowTime % 1.25f > 1.125f) {
					c.reticleRendered = false;
				}
			}
			renderCreature(sb, AbstractDungeon.player);
			Dragon dragon = DragonTamer.getLivingDragon();
			if (dragon != null) {
				renderCreature(sb, dragon);
			}
			for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
				if (!m.isDeadOrEscaped()) {
					renderCreature(sb, m);
				}
			}
		}

		AbstractDungeon.overlayMenu.combatDeckPanel.render(sb);
		AbstractDungeon.overlayMenu.discardPilePanel.render(sb);
		AbstractDungeon.overlayMenu.exhaustPanel.render(sb);
	}

	public static void reopen() {
		AbstractDungeon.cardRewardScreen.confirmButton.hideInstantly();
		AbstractDungeon.screen = AbstractDungeon.CurrentScreen.CARD_REWARD;

		AbstractDungeon.topPanel.unhoverHitboxes();
		AbstractDungeon.isScreenUp = true;
		AbstractDungeon.dynamicBanner.appear(TEXT[0]);
		AbstractDungeon.overlayMenu.showBlackScreen(0.5f);
		AbstractDungeon.overlayMenu.proceedButton.hide();
	}
}
