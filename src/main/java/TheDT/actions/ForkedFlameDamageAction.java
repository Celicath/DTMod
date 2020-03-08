package TheDT.actions;

import TheDT.characters.Dragon;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ForkedFlameDamageAction extends AbstractGameAction {
	public static Queue<ArrayList<AbstractMonster>> target_queue = new LinkedList<>();
	public static Queue<ArrayList<Integer>> target_damage_queue = new LinkedList<>();

	private Dragon dragon;

	public ForkedFlameDamageAction(Dragon dragon) {
		this.dragon = dragon;
		this.duration = Settings.ACTION_DUR_XFAST;
	}

	@Override
	public void update() {
		if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
			ForkedFlameDamageAction.target_queue.clear();
			ForkedFlameDamageAction.target_damage_queue.clear();
			AbstractDungeon.actionManager.clearPostCombatActions();
			isDone = true;
			return;
		}
		if (target_queue.isEmpty()) {
			return;
		}

		ArrayList<AbstractMonster> targets = target_queue.poll();
		ArrayList<Integer> targets_damage = target_damage_queue.poll();

		for (int i = 0; i < targets.size(); i++) {
			AbstractMonster target = targets.get(i);
			target.tint.color.set(Color.RED);
			target.tint.changeColor(Color.WHITE.cpy());

			target.damage(new DamageInfo(dragon, targets_damage.get(i)));
		}
		isDone = true;
	}
}
