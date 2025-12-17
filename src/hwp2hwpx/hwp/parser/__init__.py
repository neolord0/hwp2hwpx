"""HWP parsers."""

from hwp2hwpx.hwp.parser.body_text import parse_body_text, parse_section
from hwp2hwpx.hwp.parser.doc_info import parse_doc_info
from hwp2hwpx.hwp.parser.record import (
    Record,
    RecordHeader,
    parse_all_records,
    parse_record_header,
    parse_records_as_objects,
    parse_records_with_children,
)

__all__ = [
    "parse_doc_info",
    "parse_body_text",
    "parse_section",
    "Record",
    "RecordHeader",
    "parse_record_header",
    "parse_all_records",
    "parse_records_with_children",
    "parse_records_as_objects",
]
