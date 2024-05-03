import modifyIcon from "../../images/orderlist/modify.svg";
import deleteIcon from "../../images/orderlist/delete.svg";
import testmilk from "../../images/management/testmilk.jpg";

import ExcelJS from "exceljs";

const StockList = [
    // 테스트용
    {
        image: testmilk,
        name: "우유(1L)",
        expiration: "2024-04-27",
        amout: 3,
        PRcount: 5,
    },
    {
        image: testmilk,
        name: "우유(1L)",
        expiration: "2024-04-29",
        amout: 7,
        PRcount: 5,
    },
    {
        image: testmilk,
        name: "우유(1L)",
        expiration: "2024-05-02",
        amout: 11,
        PRcount: 5,
    },
    {
        image: testmilk,
        name: "원두(2KG)",
        expiration: "2024-04-27",
        amout: 8,
        PRcount: 3,
    },
    {
        image: testmilk,
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

const fileselect = (e) => {
    const selectedfile = e.target.files[0]; // 선택된 파일 가져오기

    //파일 읽기
    const workbook = new ExcelJS.Workbook();
    const render = new FileReader();

    render.onload = async (e) => {
        const data = new Uint8Array(e.target.result);
        await workbook.xlsx.load(data); // 엑셀 파일 로드

        const worksheet = workbook.getWorksheet(1);

        // 여기서 데이터 처리해야함
        worksheet.eachRow((row, rowNumber) => {
            console.log(`Row ${rowNumber} = ${JSON.stringify(row.values)}`);
        });
    };

    // 파일을 ArrayBuffer로 읽음
    render.readAsArrayBuffer(selectedfile);
};

const StockTable = () => {
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
                <input type="file" onChange={fileselect} />
            </div>

            <div
                className="flex flex-col overflow-auto"
                style={{ height: "35rem" }}
            >
                <div className="grid grid-cols-3 rounded-sm bg-gray-2 dark:bg-meta-4 sm:grid-cols-5">
                    <div className="p-2.5 xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            품목명
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            유통기한
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            총량
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            발주 예정 수량
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            수정/삭제
                        </h5>
                    </div>
                </div>

                {StockList.map((stock, key) => (
                    <div
                        className={`grid grid-cols-3 sm:grid-cols-5 ${
                            key === StockList.length - 1
                                ? ""
                                : "border-b border-stroke dark:border-strokedark"
                        }`}
                        key={key}
                    >
                        <div className="flex items-center gap-3 p-2.5 xl:p-5">
                            <div className="flex-shrink-0 h-1- w-10">
                                <img src={stock.image} alt="tmp"></img>
                            </div>
                            <p className="hidden text-black dark:text-white sm:block">
                                {stock.name}
                            </p>
                        </div>

                        <div className="flex items-center justify-center p-2.5 xl:p-5">
                            <p className="text-black dark:text-white">
                                {stock.expiration}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {stock.amout}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {stock.PRcount}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <button className="mr-2">
                                <img src={modifyIcon} alt="Modify" />
                            </button>
                            <button>
                                <img src={deleteIcon} alt="delete" />
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default StockTable;
