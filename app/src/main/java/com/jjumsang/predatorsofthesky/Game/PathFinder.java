package com.jjumsang.predatorsofthesky.Game;

import android.accounts.AccountManager;
import android.util.Log;

import com.jjumsang.predatorsofthesky.Game.UnitDirect.Unit;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitValue;
import com.jjumsang.predatorsofthesky.Values.MapState;
import com.jjumsang.predatorsofthesky.immortal.Vec2;

import java.util.ArrayList;
import java.util.Arrays;

public class PathFinder {
    private Vec2 m_dest;
    private final ArrayList<Node> openNodeList = new ArrayList<Node>();
    private int map[][]=UnitValue.m_bmap; //걸을수있는 맵인지 판정
    private Node nodeMap[][]; //맵 노드
    private int width;
    private int height;

    public PathFinder() {
    }

    public Node find(Vec2 origin, Vec2 dest) //나의 오브젝트,목표 오브젝트
    {
        UnitValue.testcount+=1;
        //Log.e("finding path",origin+" to "+dest);
        m_dest = dest;

        Node firstNode = new Node(origin);
        nodeMap[origin.x][origin.y] = firstNode;

        for(int i=0;i<height;i++)
        {
            Arrays.fill(nodeMap[i], null);
        }

        openNodeList.clear();
        openNodeList.add(firstNode);

        for (; ; ) {
            if (openNodeList.isEmpty()) return null;
            Node best = getBestNode();
            //Log.i("msg", best.m_pos+" -> "+dest.Postion);
            if (best.m_pos.equals(m_dest)) return best;
            openNode(best);
        }
    }

    public void LoadMap(int maps[][]) {
        map = UnitValue.m_bmap;
        width = maps.length;
        height = maps[0].length;
        nodeMap = new Node[width][height];
    }

    private Node getBestNode() {
        int minWeight = Integer.MAX_VALUE;
        int minNodeId = -1;
        for (int i = 0; i < openNodeList.size(); i++) {

            try {
                int fast = openNodeList.get(i).fast;
                if (fast >= minWeight) continue;
                minWeight = fast;
                minNodeId = i;
            }
            catch(NullPointerException e)
            {

            }
        }

        if (minNodeId == -1) throw new AssertionError("최소 노드를 못찾았다?");
        try {
            return openNodeList.remove(minNodeId);
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    private void openNode(Node parent) {
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x == 0 && y == 0) continue;
                Vec2 pos = new Vec2(parent.m_pos);
                pos.add(new Vec2(x, y));

                if(pos.x < 0) continue;
                if(pos.y < 0) continue;
                if(pos.x >= width) continue;
                if(pos.y >= height) continue;

                // 가중치 구하기
                int g;
                switch (map[pos.x][pos.y]) {
                    case MapState.Move: // 이동
                        g=10;
                        break;
                    case MapState.NotMove: // 이동불가
                        g=50000;
                        break;
                    case MapState.ElseMove:
                        g = 1000;
                        break;
                    case 3: // 이거
                        g = 5000;
                        break;
                    default:
                        g = 100000;
                        break;
                }
                g = x * x + y * y == 2 ? g * 1414 / 1000 : g;

                Node node = new Node(pos, g, parent);
                Node oldNode = nodeMap[pos.x][pos.y];
                if (oldNode != null) {
                    if (oldNode.fast <= node.fast) continue;
                    openNodeList.remove(oldNode);
                }
                nodeMap[pos.x][pos.y] = node;
                openNodeList.add(node);
            }
        }
    }

    public class Node {
        public final Vec2 m_pos;
        public final int fast;
        public final int G;
        public final Node parentNode;

        public Node(Vec2 pos) {
            this.m_pos = new Vec2(pos);
            this.G = 0;
            this.fast = 0;
            this.parentNode = null;
        }

        public Node(Vec2 pos, int Gplus, Node parentNode) {
            this.m_pos = new Vec2(pos);
            this.G = parentNode.G + Gplus;
            this.fast = this.G + m_dest.getDistance(pos) * 10;
            this.parentNode = parentNode;
        }

        public Node(Node copy) {
            m_pos = new Vec2(copy.m_pos);
            G = copy.G;
            fast = copy.fast;
            parentNode = copy.parentNode;
        }

    }
}
