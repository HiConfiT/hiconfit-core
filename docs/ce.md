## ce-package

| **groupID**    | **_at.tugraz.ist.ase_** |
|----------------|----------------------|
| **artifactID** | **_ce_**             |
| **version**    |  |

Giai đoạn khởi tạo:
Compact mode:
1. Khởi tạo có translator và/hoặc writer
Control mode:
1. Khởi tạo không có translator và/hoặc writer
2. Gán translator
3. Gán writer

Giai đoạn sử dụng:
Compact mode:
- findAllSolutions
- findSolutions
Control mode:
1. initializeWithKB or initializeWithNotKB
2. setVVO
3. setRequirement
4. find
5. removeRequirement
6. clearVVO
7. reset


Quy trình các câu lệnh như sau:

// Tạo một configurator
Configurator configurator = new Configurator(kb, true, new FMSolutionTranslator());

// Initilize the configurator with KB or with not(KB)
configurator.initializeWithKB();
// config.initializeWithNotKB();

// Identify solutions

// Reset the configurator
configurator.reset(); 

Sau khi reset, nếu thực hiện tìm lại thì sẽ lấy lại các solutions cũ.

Example 1: Sử dụng vòng lặp tìm cho đến khi nào hết solutions thì dùng

Configurator configurator = new Configurator(kb, true, new FMSolutionTranslator());
configurator.initializeWithKB();

// Identify solutions
while (configurator.find(1, 0, null)) {
    // Do something with the solution
}

configurator.reset(); 