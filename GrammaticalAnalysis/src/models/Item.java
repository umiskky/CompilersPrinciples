package models;

import java.util.List;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/17
 */
public class Item {
	int id;
	int point = 0;
	String leftPart;
	List<String> rightParts;

	public Item(Item item) {
		this.point = item.point + 1;
		this.leftPart = item.leftPart;
		this.rightParts = item.rightParts;
		this.id = item.getId();
	}

	public Item(Production production) {
		leftPart = production.leftPart;
		production.rightParts.remove("~");
		rightParts = production.rightParts;
		this.id = production.getId();
	}

//	public boolean productionEquals(Item item) {
//		boolean judge = false;
//		if (rightParts.size() == item.rightParts.size()) {
//			int i;
//			for (i = 0; i < rightParts.size(); i++) {
//				if (!rightParts.get(i).equals(item.rightParts.get(i))) {
//					break;
//				}
//			}
//			if (i == rightParts.size()) {
//				judge = true;
//			}
//			return judge && leftPart.equals(item.leftPart);
//		}
//		return false;
//	}

	public int getPoint() {
		return point;
	}

	public int getId() {
		return id;
	}

	public String getLeftPart() {
		return leftPart;
	}

	public List<String> getRightParts() {
		return rightParts;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(leftPart);
		s.append("->");
		for (int k = 0; k < rightParts.size(); k++) {
			if (point == k) {
				s.append("·");
			}
			s.append(rightParts.get(k));
		}
		if (point == rightParts.size()) {
			s.append("·");
		}
		return s.toString();
	}
}
