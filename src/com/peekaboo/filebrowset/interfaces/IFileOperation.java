package com.peekaboo.filebrowset.interfaces;

/**
 * 
 * @author peekaboo
 *
 */
public interface IFileOperation
{
    void onOpenWith();
    void onRename();
    void onCopy();
    void onMove();
    void onDelete();
    void onProperty();
    void onCreateFile();
    void onCreateFolder();
}
