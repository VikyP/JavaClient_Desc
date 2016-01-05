/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board.events;

import java.awt.Dimension;
import java.util.ArrayList;
import receiver_board.shapes.IShapeAction;

/**
 *
 * @author viky
 */
public interface IGraphChanged
{
    void getNewGraph(byte numberPage,Dimension D, ArrayList<IShapeAction> SA);
}
