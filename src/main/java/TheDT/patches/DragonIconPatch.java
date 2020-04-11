package TheDT.patches;

import TheDT.cards.AbstractDTCard;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import static TheDT.cards.AbstractDTCard.DRAGON_ICON_HEIGHT;
import static TheDT.cards.AbstractDTCard.DRAGON_ICON_WIDTH;

public class DragonIconPatch {
	@SpirePatch(clz = AbstractCard.class, method = "renderImage")
	public static class AbstractCardPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected) {
			if (__instance instanceof AbstractDTCard) {
				AbstractDTCard c = (AbstractDTCard) __instance;
				if (c.dtCardTarget != AbstractDTCard.DTCardTarget.DEFAULT && c.type == AbstractCard.CardType.ATTACK) {
					float scale = c.drawScale * Settings.scale * 0.6f;
					sb.draw(c.getDragonIconTexture(),
							c.current_x - DRAGON_ICON_WIDTH / 2.0f - AbstractDTCard.IMG_WIDTH * 0.45f * c.drawScale,
							c.current_y - DRAGON_ICON_HEIGHT / 2.0f + AbstractDTCard.IMG_HEIGHT * 0.3f * c.drawScale,
							DRAGON_ICON_WIDTH / 2.0f,
							DRAGON_ICON_HEIGHT / 2.0f,
							DRAGON_ICON_WIDTH,
							DRAGON_ICON_HEIGHT,
							scale,
							scale,
							c.angle, 0, 0, DRAGON_ICON_WIDTH, DRAGON_ICON_HEIGHT, false, false);
				}
			}
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "render")
	public static class SingleCardViewPopupPatch {
		@SpirePostfixPatch
		public static void Postfix(SingleCardViewPopup __instance, SpriteBatch sb) {
			AbstractCard card = (AbstractCard) ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
			if (card instanceof AbstractDTCard) {
				AbstractDTCard c = (AbstractDTCard) card;

				if (c.dtCardTarget != AbstractDTCard.DTCardTarget.DEFAULT && c.type == AbstractCard.CardType.ATTACK) {
					float scale = Settings.scale * 1.2f;
					sb.draw(c.getDragonIconTexture(),
							Settings.WIDTH / 2.0f - DRAGON_ICON_WIDTH / 2.0f - AbstractDTCard.IMG_WIDTH * 0.9f,
							Settings.HEIGHT / 2.0f - DRAGON_ICON_HEIGHT / 2.0f + AbstractDTCard.IMG_HEIGHT * 0.6f,
							DRAGON_ICON_WIDTH / 2.0f,
							DRAGON_ICON_HEIGHT / 2.0f,
							DRAGON_ICON_WIDTH,
							DRAGON_ICON_HEIGHT,
							scale,
							scale,
							0, 0, 0, DRAGON_ICON_WIDTH, DRAGON_ICON_HEIGHT, false, false);
				}
			}
		}
	}
}
