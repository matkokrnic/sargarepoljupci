import { LoginChecker } from "../components/Loginchecker.jsx";
import {NavBar} from "../components/NavBar.jsx";

export function HomePage() {
  return (
      <div className="h-full bg-gray-100">
        <div className="h-full">
            <div className="min-h-full">
               <NavBar />
                <header className="bg-white shadow">
                <div className="px-4 py-6 mx-auto max-w-7xl sm:px-6 lg:px-8">
                    <h1 className="text-3xl font-bold tracking-tight text-gray-900">Nadzorna ploča</h1>
                </div>
                </header>
                <main>
                <LoginChecker />
                <div className="py-6 mx-auto max-w-7xl sm:px-6 lg:px-8">{/*tu sadrzaj*/}</div>
                </main>
            </div>
        </div>
    </div>
  )
}