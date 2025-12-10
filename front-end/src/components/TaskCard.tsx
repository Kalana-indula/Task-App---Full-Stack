import type { Task } from "../types.ts";

interface TaskProps {
    task: Task;
    onCompleted: (taskId: number) => void;
}

const TaskCard = ({ task, onCompleted }: TaskProps) => {
    return (
        <div className="bg-white rounded-lg shadow-md p-6 mb-4 flex items-start justify-between">
            <div className="flex-1">
                <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    {task.title}
                </h3>
                <p className="text-gray-600 text-sm">{task.description}</p>
            </div>
            <button
                onClick={() => onCompleted(task.id)}
                className="ml-4 bg-green-100 hover:bg-green-200 text-green-700 font-medium px-6 py-2 rounded-md transition-colors duration-200 flex-shrink-0 hover:cursor-pointer"
            >
                Done
            </button>
        </div>
    );
};

export default TaskCard;
