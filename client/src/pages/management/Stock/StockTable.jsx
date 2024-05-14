import ExcelJS from "exceljs";
import StockListInfo from "./StockListInfo";

const StockList = [
    // 테스트용
    {
        name: "우유(1L)",
        expiration: "2024-04-27",
        amout: 3,
        PRcount: 5,
    },
    {
        name: "우유(1L)",
        expiration: "2024-04-29",
        amout: 7,
        PRcount: 5,
    },
    {
        name: "우유(1L)",
        expiration: "2024-05-02",
        amout: 11,
        PRcount: 5,
    },
    {
        name: "원두(2KG)",
        expiration: "2024-04-27",
        amout: 8,
        PRcount: 3,
    },
    {
        name: "우유(1L)",
        expiration: "2024-04-27",
        amout: 2,
        PRcount: 0,
    },
];

const excelsample = async () => {
    // 엑셀 워크북 생성
    const workbook = new ExcelJS.Workbook();

    // 워크시트 생성
    const worksheet = workbook.addWorksheet("Sample");

    worksheet.getColumn(1).width = 15;
    worksheet.getColumn(2).width = 15;
    worksheet.getColumn(3).width = 15;
    worksheet.getColumn(4).width = 15;
    worksheet.getColumn(5).width = 15;

    // 행 추가 (품목명, 유통기한, 총량, 발주예정수량)
    const firstRow = worksheet.addRow([
        "품목명",
        "유통기한",
        "총량",
        "발주예정수량",
        "*추가할 재고를 예시 형식에 맞게 2행부터 작성",
    ]);

    ["A1", "B1", "C1", "D1"].map((key) => {
        worksheet.getCell(key).fill = {
            type: "pattern",
            pattern: "solid",
            fgColor: { argb: "59b0fc" },
        };
        return null;
    });

    worksheet.getCell("E1").font = {
        bold: true,
    };

    StockList.map((stock, idx) => {
        const Row = worksheet.addRow([]);
        Row.getCell(1).value = stock.name;
        Row.getCell(2).value = stock.expiration;
        Row.getCell(3).value = stock.amout;
        Row.getCell(4).value = stock.PRcount;

        return null;
    });

    // 엑셀 파일로 저장
    workbook.xlsx.writeBuffer().then((buffer) => {
        const blob = new Blob([buffer], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = "sample.xlsx";
        a.click();
    });
};

const StockTable = ({ stockInfo }) => {
    if (!stockInfo || stockInfo.length === 0) {
        return <div>No stock data available.</div>;
    }
    return (
        <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
            <div className="flex-col gap-3 flex sm:flex-row sm:items-center sm:justify-between">
                <h4 className="mb-6 text-xl font-semibold text-black dark:text-white flex-row">
                    Stock List
                </h4>
                <button
                    className="bg-transparent hover:bg-blue-500 text-blue-700 font-semibold hover:text-white py-2 px-4 border border-blue-500 hover:border-transparent rounded"
                    onClick={excelsample}
                >
                    엑셀 에제
                </button>
            </div>

            <div
                className="flex flex-col overflow-auto"
                style={{ height: "35rem" }}
            >
                <div className="grid grid-cols-5 rounded-sm bg-gray-2 dark:bg-meta-4 sm:grid-cols-5">
                    <div className="p-2.5  text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-sm">
                            품목명
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-sm">
                            유통기한
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-sm">
                            총량
                        </h5>
                    </div>
                    <div className="p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-sm">
                            발주 예정 수량
                        </h5>
                    </div>
                    <div className="p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-sm">
                            수정/삭제
                        </h5>
                    </div>
                </div>

                {stockInfo.map((stock, index) => (
                    <StockListInfo
                        key={index}
                        stock={stock}
                        isLastItem={index === stockInfo.length - 1}
                    />
                ))}
            </div>
        </div>
    );
};

export default StockTable;
