package com.myfridge.controller;

import com.myfridge.entity.Note;
import com.myfridge.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        List<Note> notes = noteService.getAllActiveNotes();
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createNote(@RequestBody Note note) {
        Note created = noteService.createNote(note);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "贴好了。冰箱轻轻嗡了一声，好像收到了。");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Note note = noteService.getNoteById(id);
        if (note != null) {
            return ResponseEntity.ok(note);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/magnet")
    public ResponseEntity<Map<String, Object>> addMagnet(@PathVariable Long id) {
        Note updated = noteService.addMagnet(id);
        Map<String, Object> response = new HashMap<>();
        if (updated != null) {
            response.put("success", true);
            response.put("magnetCount", updated.getMagnetCount());
        } else {
            response.put("success", false);
            response.put("message", "磁铁掉地上了，再试一次？");
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/position")
    public ResponseEntity<Note> updatePosition(@PathVariable Long id,
                                               @RequestParam Double x,
                                               @RequestParam Double y) {
        Note updated = noteService.updatePosition(id, x, y);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "撕下来");
        return ResponseEntity.ok(response);
    }
}
