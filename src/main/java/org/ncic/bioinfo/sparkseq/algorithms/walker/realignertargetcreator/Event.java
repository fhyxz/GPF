package org.ncic.bioinfo.sparkseq.algorithms.walker.realignertargetcreator;

import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;

import java.util.ArrayList;

/**
 * Author: wbc
 */
public class Event {
    public int furthestStopPos;

    GenomeLoc loc;
    int eventStartPos;
    int eventStopPos;
    EventType type;
    ArrayList<Integer> pointEvents = new ArrayList<Integer>();

    public Event(GenomeLoc loc, int furthestStopPos, EventType type) {
        this.loc = loc;
        this.furthestStopPos = furthestStopPos;
        this.type = type;

        if (type == EventType.INDEL_EVENT || type == EventType.BOTH) {
            eventStartPos = loc.getStart();
            eventStopPos = loc.getStop();
        } else {
            eventStartPos = -1;
            eventStopPos = -1;
        }

        if (type == EventType.POINT_EVENT || type == EventType.BOTH) {
            pointEvents.add(loc.getStart());
        }
    }

    public void merge(Event e) {

        // merges only get called for events with certain types
        if (e.type == EventType.INDEL_EVENT || e.type == EventType.BOTH) {
            if (eventStartPos == -1)
                eventStartPos = e.eventStartPos;
            eventStopPos = e.eventStopPos;
            furthestStopPos = e.furthestStopPos;
        }

        if (e.type == EventType.POINT_EVENT || e.type == EventType.BOTH) {
            int newPosition = e.pointEvents.get(0);
            if (pointEvents.size() > 0) {
                int lastPosition = pointEvents.get(pointEvents.size() - 1);
                if (newPosition - lastPosition < RealignerTargetCreator.windowSize) {
                    eventStopPos = Math.max(eventStopPos, newPosition);
                    furthestStopPos = e.furthestStopPos;

                    if (eventStartPos == -1)
                        eventStartPos = lastPosition;
                    else
                        eventStartPos = Math.min(eventStartPos, lastPosition);
                } else if (eventStartPos == -1 && e.eventStartPos != -1) {
                    eventStartPos = e.eventStartPos;
                    eventStopPos = e.eventStopPos;
                    furthestStopPos = e.furthestStopPos;
                }
            }
            pointEvents.add(newPosition);
        }
    }

    public boolean isReportableEvent(GenomeLocParser genomeLocParser) {
        return genomeLocParser.isValidGenomeLoc(
                loc.getContig(), eventStartPos, eventStopPos, true)
                && eventStopPos >= 0
                && eventStopPos - eventStartPos < RealignerTargetCreator.maxIntervalSize;
    }

    public GenomeLoc getLoc(GenomeLocParser genomeLocParser) {
        return genomeLocParser.createGenomeLoc(loc.getContig(), eventStartPos, eventStopPos);
    }
}
