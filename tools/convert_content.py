import json
from pathlib import Path

from openpyxl import load_workbook


PROJECT_ROOT = Path(__file__).resolve().parent.parent

SOURCE_FILE = (
    PROJECT_ROOT.parent
    / "OnlySex-Content-Database.xlsx"
)

OUTPUT_FILE = (
    PROJECT_ROOT
    / "shared"
    / "src"
    / "commonMain"
    / "composeResources"
    / "files"
    / "content.json"
)

SHEET_NAMES = {
    "Tasks": "tasks",
    "Penalties": "penalties",
    "Totems": "totems",
    "Toys": "toys",
    "Categories": "categories",
}

BOOLEAN_FIELDS = {
    "penetrative",
    "orgasm_allowed",
    "enabled",
}

LIST_FIELDS = {
    "toy_ids",
}


def convert_value(field_name, value):
    if value is None:
        return None

    if field_name in BOOLEAN_FIELDS:
        return bool(value)

    if field_name in LIST_FIELDS:
        if value == "":
            return []

        return [
            item.strip()
            for item in str(value).replace(";", ",").split(",")
            if item.strip()
        ]

    return value


def read_sheet(worksheet):
    rows = list(worksheet.iter_rows(values_only=True))

    if not rows:
        return []

    headers = rows[0]
    records = []

    for row in rows[1:]:
        if not any(value is not None for value in row):
            continue

        record = {}

        for header, value in zip(headers, row):
            if header is None:
                continue

            record[header] = convert_value(
                field_name=header,
                value=value,
            )

        records.append(record)

    return records


def main():
    if not SOURCE_FILE.exists():
        raise FileNotFoundError(
            f"Excel database not found: {SOURCE_FILE}"
        )

    workbook = load_workbook(
        filename=SOURCE_FILE,
        read_only=True,
        data_only=True,
    )

    content = {}

    for sheet_name, json_name in SHEET_NAMES.items():
        if sheet_name not in workbook.sheetnames:
            raise ValueError(
                f"Required sheet is missing: {sheet_name}"
            )

        content[json_name] = read_sheet(
            workbook[sheet_name]
        )

    OUTPUT_FILE.parent.mkdir(
        parents=True,
        exist_ok=True,
    )

    with OUTPUT_FILE.open(
        mode="w",
        encoding="utf-8",
    ) as output:
        json.dump(
            content,
            output,
            ensure_ascii=False,
            indent=2,
        )

    print(f"Created: {OUTPUT_FILE}")

    for name, records in content.items():
        print(f"{name}: {len(records)}")


if __name__ == "__main__":
    main()