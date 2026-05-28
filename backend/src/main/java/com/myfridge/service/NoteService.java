package com.myfridge.service;

import com.myfridge.entity.Note;
import com.myfridge.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    // 获取所有活跃的便签
    public List<Note> getAllActiveNotes() {
        return noteRepository.findByIsActiveTrueOrderByCreatedAtDesc();
    }

    // 创建新便签
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    // 获取单个便签
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    // 增加磁铁数量
    public Note addMagnet(Long id) {
        Note note = noteRepository.findById(id).orElse(null);
        if (note != null) {
            note.setMagnetCount(note.getMagnetCount() + 1);
            return noteRepository.save(note);
        }
        return null;
    }

    // 更新便签位置
    public Note updatePosition(Long id, Double x, Double y) {
        Note note = noteRepository.findById(id).orElse(null);
        if (note != null) {
            note.setPositionX(x);
            note.setPositionY(y);
            return noteRepository.save(note);
        }
        return null;
    }

    // 删除便签（软删除）
    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id).orElse(null);
        if (note != null) {
            note.setIsActive(false);
            noteRepository.save(note);
        }
    }
}
