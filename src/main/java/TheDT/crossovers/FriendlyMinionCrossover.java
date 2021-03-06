package TheDT.crossovers;

import TheDT.patches.SkillbookGameState;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.characters.AbstractPlayerWithMinions;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

import java.util.ArrayList;

public class FriendlyMinionCrossover {
	public static void addFriendlyMinionStates(ArrayList<SkillbookGameState.CharacterState> state) {
		if (AbstractDungeon.player instanceof AbstractPlayerWithMinions) {
			for (AbstractMonster m : ((AbstractPlayerWithMinions) AbstractDungeon.player).minions.monsters) {
				state.add(new SkillbookGameState.CharacterState(m));
			}
		}
	}

	public static void giveFriendlyMinionsBlock(int block) {
		if (AbstractDungeon.player instanceof AbstractPlayerWithMinions) {
			for (AbstractMonster m : ((AbstractPlayerWithMinions) AbstractDungeon.player).minions.monsters) {
				AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, m, block, true));
			}
		}
	}

	public static void clearMinions(AbstractPlayer player) {
		BasePlayerMinionHelper.clearMinions(player);
	}
}
