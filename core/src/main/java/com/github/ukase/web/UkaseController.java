/*
 * Copyright (c) 2018 Pavel Uvarov <pauknone@yahoo.com>
 *
 * This file is part of Ukase.
 *
 *  Ukase is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.ukase.web;

import com.github.ukase.async.AsyncManager;
import com.github.ukase.service.HtmlRenderer;
import com.github.ukase.service.XlsxRenderer;
import com.github.ukase.toolkit.TemplateListenable;
import com.github.ukase.toolkit.TemplateListener;
import com.github.ukase.toolkit.render.RenderTaskBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
class UkaseController {
    private final HtmlRenderer htmlRenderer;
    private final Collection<TemplateListenable> templateListenables;
    private final AsyncManager asyncManager;
    private final XlsxRenderer xlsxRenderer;
    private final RenderTaskBuilder taskBuilder;

    //<editor-fold desc="State API method">
    /*================================================================================================================
     ==============================================  State API controllers   =========================================
     =================================================================================================================*/
    @RequestMapping(value = "/pdf/{template}", method = RequestMethod.HEAD)
    public @ResponseBody
    DeferredState checkTemplate(@PathVariable String template) {
        DeferredState state = new DeferredState();
        TemplateListener listener = TemplateListener.templateListener(template,
                test -> state.setResult(translateState(test)));
        templateListenables.forEach(l -> l.registerListener(listener));
        return state;
    }
    //</editor-fold>

    //<editor-fold desc="Sync render API methods">
    /*================================================================================================================
     ==============================================  Renderer API controllers  =======================================
     =================================================================================================================*/
    @RequestMapping(value = "/html", method = RequestMethod.POST)
    public ResponseEntity<String> generateHtml(@RequestBody @Valid UkasePayload payload) {
        String result = htmlRenderer.render(payload);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/pdf", method = RequestMethod.POST, produces = "application/pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestBody @Valid UkasePayload payload) {
        log.debug("Generate PDF POST for '{}' :\n{}\n", payload.getIndex(), payload.getData());
        return ResponseEntity.ok(taskBuilder.build(payload).call());
    }

    @RequestMapping(value = "/xlsx", method = RequestMethod.POST,
            produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> generateXlsx(@RequestBody @Valid UkasePayload payload) {
        log.debug("Generate XLSX POST for '{}' :\n{}\n", payload.getIndex(), payload.getData());
        String html = htmlRenderer.render(payload);
        log.debug("Prepared xhtml:\n{}\n", html);
        return ResponseEntity.ok(xlsxRenderer.render(html));
    }
    //</editor-fold>

    //<editor-fold desc="pdf bulk/async API methods">
    /*================================================================================================================
     ============================================== bulk/async API controllers =======================================
     =================================================================================================================*/

    @RequestMapping(value = "/bulk/sync", method = RequestMethod.POST,
            produces = "application/pdf", consumes = "text/json")
    public ResponseEntity<byte[]> renderBulk(@RequestBody List<UkasePayload> payloads) throws InterruptedException {
        return ResponseEntity.ok(asyncManager.processOrder(payloads));
    }
    //</editor-fold>

    //<editor-fold desc="Private utility methods">
    /*================================================================================================================
     ==============================================  private utilities  ==============================================
     =================================================================================================================*/
    private ResponseEntity<Object> translateState(boolean selectedTemplateUpdated) {
        if (selectedTemplateUpdated) {
            return new ResponseEntity<>("updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }
    //</editor-fold>
}
