/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board.events;

/**
 *
 * @author viky
 */
public interface ITextChanged
{
    /**
     * Обновление текстовых данных
     * @param numberPage номер доски (страницы)
     * @param numberLine номер строчки
     * @param fontHeight высота шрифта
     * @param s текст
     */
    void getNewText(byte numberPage, byte numberLine, byte fontHeight, String s);
}
